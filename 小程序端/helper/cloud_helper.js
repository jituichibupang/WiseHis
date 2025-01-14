/**
 * Notes: Http操作类库
 * Ver : CCMiniCloud Framework 2.3.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2020-11-14 07:48:00 
 */

const helper = require('./helper.js');
const cacheHelper = require('./cache_helper.js');
const constants = require('../comm/constants.js');
const pageHelper = require('../helper/page_helper.js');
const setting = require('../setting/setting.js');

const CODE = {
	SUCC: 200,
	SVR: 500, //服务器错误  
	LOGIC: 1600, //逻辑错误 
	DATA: 1301, // 数据校验错误 
	HEADER: 1302, // header 校验错误   

	TOKEN_ERROR: 2401 //需要登陆
};

function callCloudSumbitAsync(route, params = {}, options) {
	if (!helper.isDefined(options)) options = {
		hint: false
	}
	if (!helper.isDefined(options.hint)) options.hint = false;
	return callCloud(route, params, options)
}

async function callCloudSumbit(route, params = {}, options = { title: '提交中...' }) {
	if (!helper.isDefined(options)) options = {
		title: '提交中..'
	}
	if (!helper.isDefined(options.title)) options.title = '提交中..';
	return await callCloud(route, params, options);
}

async function callCloudData(route, params = {}, options) {
	if (!helper.isDefined(options)) options = {
		title: '加载中..'
	}

	if (!helper.isDefined(options.title)) options.title = '加载中..';
	let result = await callCloud(route, params, options).catch(err => {
		return null;
	});

	if (result && helper.isDefined(result.data)) {
		result = result.data;
		if (result === null || result === undefined) {
			result = null;
		}
		else if (Array.isArray(result)) {
			// 数组处理
		} else if (Object.keys(result).length == 0) {
			result = null; //对象处理
		}
	}
	return result;
}

function getToken(route) {
	let token = '';
	let sub = "";

	if (route.indexOf('admin/') > -1) {
		// 管理员token
		let admin = cacheHelper.get(constants.CACHE_ADMIN);
		if (admin && admin.token) token = admin.token;
		sub = "admin";

	} else if (route.indexOf('work/') > -1) {
		// 工作人员token
		let work = cacheHelper.get(constants.CACHE_WORK);
		if (work && work.token) token = work.token;
		sub = "work";
	}
	else {
		//普通用户
		let user = cacheHelper.get(constants.CACHE_TOKEN);
		if (user && user.id) token = user.token;
		sub = "cust";
	}
	return { token, sub };
}

function callCloud(route, params = {}, options) {

	let title = '加载中';
	let hint = true; //数据请求时是否mask提示 

	// 标题
	if (helper.isDefined(options) && helper.isDefined(options.title))
		title = options.title;

	// 是否给提示
	if (helper.isDefined(options) && helper.isDefined(options.hint))
		hint = options.hint;

	// 是否输出错误并处理
	if (helper.isDefined(options) && helper.isDefined(options.doFail))
		doFail = options.doFail;

	if (hint) {
		if (title == 'bar')
			wx.showNavigationBarLoading();
		else
			wx.showLoading({
				title: title,
				mask: true
			})
	}


	return new Promise(function (resolve, reject) {

		let PID = pageHelper.getPID();
		let url = setting.API_URL;

		if (!route.startsWith("/")) route = "/" + route;

		if (PID)
			url += '/' + PID + route;
		else
			url += route;

		let { token, sub } = getToken(route);

		wx.request({
			url,
			header: {
				token,
				sub
			},
			method: 'POST',
			data: params,
			success: function (res) {
				if (!_requestHanlder(res, reject, hint, false, sub)) return;

				resolve(res.data);
			},
			fail: function (err) {
				if (hint) {
					console.log(err)
					wx.showModal({
						title: '',
						content: '网络故障，请稍后重试',
						showCancel: false
					});
				}
				reject(err.errMsg);
				return;
			},
			complete: function (res) {
				if (hint) {
					if (title == 'bar')
						wx.hideNavigationBarLoading();
					else
						wx.hideLoading();
				}
				// complete
			}
		});
	});
}

async function dataList(that, listName, route, params, options, isReverse = false) {

	console.log('dataList begin');

	if (!helper.isDefined(that.data[listName]) || !that.data[listName]) {
		let data = {};
		data[listName] = {
			page: 1,
			size: 20,
			list: [],
			count: 0,
			total: 0,
			oldTotal: 0
		};
		that.setData(data);
	}

	//改为后台默认控制
	//if (!helper.isDefined(params.size))
	//	params.size = 20;

	if (!helper.isDefined(params.isTotal))
		params.isTotal = true;

	let page = params.page;
	let count = that.data[listName].count;
	if (page > 1 && page > count) {
		wx.showToast({
			duration: 500,
			icon: 'none',
			title: '没有更多数据了',
		});
		return;
	}

	for (let key in params) {
		if (!helper.isDefined(params[key]))
			delete params[key];
	}

	let oldTotal = 0;
	if (that.data[listName] && that.data[listName].total)
		oldTotal = that.data[listName].total;
	params.oldTotal = oldTotal;

	// 云函数调用 
	await callCloud(route, params, options).then(function (res) {
		console.log('cloud begin');

		let dataList = res.data;
		let tList = that.data[listName].list;

		if (dataList.page == 1) {
			tList = res.data.list;
		} else if (dataList.page > that.data[listName].page) {
			if (isReverse)
				tList = res.data.list.concat(tList);
			else
				tList = tList.concat(res.data.list);
		} else
			return;

		dataList.list = tList;
		let listData = {};
		listData[listName] = dataList;

		that.setData(listData);

		console.log('cloud END');
	}).catch(err => {
		console.log(err)
	});

	console.log('dataList END');

}


async function transTempPicOne(filePath, mark) {
	if (!filePath.includes('tmp') && !filePath.includes('temp') && !filePath.includes('wxfile')) return; 

	// 是否为临时文件
	if (filePath.includes('tmp') || filePath.includes('temp') || filePath.includes('wxfile')) {

		let PID = pageHelper.getPID();
		let route = pageHelper.getCurrentPageURL();

		if (!route.includes(PID)) route = pageHelper.getParentPageURL();

		let url = setting.API_URL;
		url += '/' + PID + "/comm/upload";

		let { token, sub } = getToken(route);

		return new Promise(function (resolve, reject) {
			wx.uploadFile({
				url,
				filePath: filePath,
				name: 'file',
				header: {
					token,
					sub
				},
				formData: ({ //上传图片所要携带的参数 
					pid: PID
				}),
				success: function (res) {
					if (!_requestHanlder(res, reject, true, true, 'admin')) return;

					resolve(res.data.data.fileID);

				},
				fail(res) {
					wx.showModal({
						title: '',
						content: '网络故障，请稍后重试',
						showCancel: false
					});
					reject(err.errMsg);
					return;
				},
				complete: function (res) {
					wx.hideLoading();
				}
			})
		});
	}

}


function _requestHanlder(res, reject, hint, convertJson = false, sub) {
	console.log(res);

	if (convertJson)
		res.data = JSON.parse(res.data);

	if (!res || !res.data || res.statusCode != 200) {
		if (hint) {
			wx.showModal({
				title: '温馨提示',
				content: '系统开小差了，请稍后重试',
				showCancel: false
			});
		}
		reject(res.data);
		return false;
	}

	if (res.data.code == CODE.LOGIC || res.data.code == CODE.DATA) {
		// 逻辑错误&数据校验错误 
		if (hint) {
			wx.showModal({
				title: '温馨提示',
				content: res.data.msg,
				showCancel: false
			});
		}

		reject(res.data);
		return false;
	} else if (res.data.code == CODE.TOKEN_ERROR) {
		wx.showModal({
			title: '温馨提示',
			content: '未登录或您的登录已经过期，请重新登陆',
			showCancel: false,
			complete: (res) => {
				if (sub == 'cust') {
					wx.reLaunch({
						url: pageHelper.fmtURLByPID('/pages/my/login/my_login'),
					});
				}
				else if (sub == 'admin') {
					wx.reLaunch({
						url: pageHelper.fmtURLByPID('/pages/admin/index/login/admin_login'),
					});
				}
				else if (sub == 'work') {
					wx.reLaunch({
						url: pageHelper.fmtURLByPID('/pages/work/index/login/work_login'),
					});
				}
			},
			fail: (err) => {
				console.error(err)
			}
		})
		//reject(res.data);
		return false;
	}
	else if (res.data.code != CODE.SUCC) {
		if (hint) {
			wx.showModal({
				title: '温馨提示',
				content: '系统开小差了，请稍后重试',
				showCancel: false
			});
		}
		reject(res.data);
		return false;
	}
	return true;
}


function uploadOSSFile(filePath, mark = 'no') {

	const dataHelper = require('./data_helper.js');
	const timeHelper = require('./time_helper.js');
	const Base64 = require('../lib/base64');
	const Crypto = require('../lib/crypto');

	const aliOSS = setting.ALI_OSS;

	return new Promise(function (resolve, reject) {
		if (!filePath) {
			reject({
				status: false,
				err: '文件错误',
			});
			return;
		}

		// 文件名
		let ext = filePath.match(/\.[^.]+?$/)[0];
		ext = ext.toLowerCase();

		if (ext == '.jpeg') ext = '.jpg';

		let rd = dataHelper.genRandomNum(100000000, 999999999);

		const aliyunFileKey = mark + '/' + timeHelper.time('YMD') + '/' + timeHelper.time('YMDhms') + '_' + rd + ext;

		const policyBase64 = Base64.encode(JSON.stringify({
			"expiration": new Date(new Date().getTime() + aliOSS.timeout).toISOString(),
			"conditions": [
				["content-length-range", 0, 1024 * 1024 * 10]//10m 图片大小
			]
		}));
		let bytes = Crypto.util.HMAC(Crypto.util.SHA1, policyBase64, aliOSS.accessKeySecret, { asBytes: true });
		const signature = Crypto.util.bytesToBase64(bytes);
		wx.uploadFile({
			url: aliOSS.ossUrl,
			filePath: filePath,
			name: 'file',
			formData: {
				'key': aliyunFileKey,
				'OSSAccessKeyId': aliOSS.accessKeyID,
				'policy': policyBase64,
				'Signature': signature,
				'success_action_status': '200',
			},
			success: function (res) {
				console.log(res)
				if (res.data.includes('<Error>')) {
					pageHelper.showModal('图片上传失败');
					resolve('');
				}
				else
					resolve(aliOSS.url + '/' + aliyunFileKey);
			},
			fail: function (err) {
				console.error();
			},
		})
	})
}

module.exports = {
	CODE,
	dataList,
	callCloud,
	callCloudSumbit,
	callCloudData,
	callCloudSumbitAsync,
	transTempPicOne,
	uploadOSSFile
}