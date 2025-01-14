const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const helper = require('../../../../../../helper/helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');


Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,



		meetId: '',

		title: '',
		titleEn: '',
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		if (!AdminBiz.isAdmin(this)) return;

		// 附加参数 
		if (options && options.meetId) {
			//设置搜索菜单 
			this._getSearchMenu();

			this.setData({
				meetId: options.meetId,
				_params: {
					meetId: options.meetId
				}
			}, () => {
				this.setData({
					isLoad: true
				});
			});
		}

		if (options && options.title) {
			let title = decodeURIComponent(options.title);
			this.setData({
				title,
				titleEn: options.title
			});
			wx.setNavigationBarTitle({
				title: '预约名单 - ' + title
			});
		}
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () {

	},

	url: async function (e) {
		pageHelper.url(e, this);
	},




	bindCopyTap: function (e) {
		let idx = pageHelper.dataset(e, 'idx');
		let forms = this.data.dataList.list[idx].meetJoinForms;
		let time = this.data.dataList.list[idx].meetJoinDay + ' ' + this.data.dataList.list[idx].meetJoinTime;

		let ret = '';

		if (this.data.title)
			ret += `预约：${this.data.title}\r`;

		ret += `时段：${time}\r`;

		for (let k = 0; k < forms.length; k++) {
			ret += forms[k].title + '：' + forms[k].val + '\r';
		}
		wx.setClipboardData({
			data: ret,
			success(res) {
				wx.getClipboardData({
					success(res) {
						pageHelper.showNoneToast('已复制到剪贴板');
					}
				})
			}
		});

	},

	bindCheckTap: async function (e) {
		let flag = Number(pageHelper.dataset(e, 'flag'));

		let callback = async () => {
			let idx = Number(pageHelper.dataset(e, 'idx'));
			let dataList = this.data.dataList;
			let meetJoinId = dataList.list[idx].meetJoinId;
			let params = {
				meetJoinId,
				flag,
			}
			let opts = {
				title: '处理中'
			}
			try {
				await cloudHelper.callCloudSumbit('admin/meet/join/check', params, opts).then(res => {
					let cb = () => {
						const timeHelper = require('../../../../../../helper/time_helper.js');
						dataList.list[idx].meetJoinIsCheck = flag;
						dataList.list[idx].meetJoinCheckTime = timeHelper.time('Y-M-D h:m:s');

						this.setData({
							dataList
						});
					}

					pageHelper.showSuccToast('操作成功', 1000, cb);


				});
			} catch (err) {
				console.error(err);
			}
		}
		if (flag == 1)
			pageHelper.showConfirm('确认「签到核销」？', callback);
		else if (flag == 0)
			pageHelper.showConfirm('确认「取消签到」？', callback);

	},

	bindDelTap: async function (e) {

		let callback = async () => {
			let idx = Number(pageHelper.dataset(e, 'idx'));
			let dataList = this.data.dataList;
			let meetJoinId = dataList.list[idx].meetJoinId;
			let params = {
				meetJoinId
			}
			let opts = {
				title: '删除中'
			}
			try {
				await cloudHelper.callCloudSumbit('admin/meet/join/del', params, opts).then(res => {

					let cb = () => {
						let dataList = this.data.dataList;
						dataList.list.splice(idx, 1);
						dataList.total--;
						this.setData({
							dataList
						});
					}

					pageHelper.showSuccToast('删除成功', 1000, cb);
				});
			} catch (err) {
				console.error(err);
			}
		}

		pageHelper.showConfirm('确认删除该预约记录？ 删除后用户将无法查询到本预约记录', callback);


	},

	bindCommListCmpt: function (e) {
		pageHelper.commListListener(this, e);

	},

	// 修改与展示状态菜单
	_getSearchMenu: function () {

		let sortItems = [];
		let sortMenus = [
			{ label: '全部', type: 'all', value: '' },
			{ label: `已签到`, type: 'check', value: 1 },
			{ label: `未签到`, type: 'check', value: 0 }
		];
		this.setData({
			sortItems,
			sortMenus
		})


	}

})