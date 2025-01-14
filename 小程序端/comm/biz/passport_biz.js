/**
 * Notes: 注册登录模块业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2020-11-14 07:48:00 
 */

const BaseBiz = require('./base_biz.js');
const cacheHelper = require('../../helper/cache_helper.js');
const pageHelper = require('../../helper/page_helper.js');
const constants = require('../constants.js');

class PassportBiz extends BaseBiz {

	static loginSilence(that) {
		if (PassportBiz.isLogin()) {
			this.setData({ user: PassportBiz.getToken(), isLogin: true });
		}
		else {
			this.setData({ isLogin: false });
		}
	}

	// 必须登陆 可以取消(窗口形式) 
	static loginMustCancelWin(that) {
		return PassportBiz.loginCheck(true, 'cancel', that);
	}

	// 必须登陆 只能强制注册或者回上页(窗口形式)  
	static loginMustBackWin(that) {
		return PassportBiz.loginCheck(true, 'back', that);
	}

	// 获取token  
	static getToken() {
		let token = cacheHelper.get(constants.CACHE_TOKEN);
		return token || null;
	}

	// 设置token
	static setToken(token, expire = 86400) {

		if (!token) return;
		cacheHelper.set(constants.CACHE_TOKEN, token, expire);
	}

	//  获取user id 
	static getUserId() {
		let token = cacheHelper.get(constants.CACHE_TOKEN);
		if (!token) return '';
		return token.id || '';
	}

	// 获取user name 
	static getUserName() {
		let token = cacheHelper.get(constants.CACHE_TOKEN);
		if (!token) return '';
		return token.name || '';
	}

	static getStatus() {
		let token = cacheHelper.get(constants.CACHE_TOKEN);
		if (!token) return -1;
		return token.status || -1;
	}

	// 是否登录 
	static isLogin() {
		let id = PassportBiz.getUserId();
		return id ? true : false;
	}


	// 登录判断及处理
	static loginCheck(mustLogin = false, method = 'back', that = null) {
		let token = cacheHelper.get(constants.CACHE_TOKEN);
		if (token && method != 'must') {
			if (that)
				that.setData({
					isLogin: true
				});
			return true;
		} else {
			if (that) that.setData({
				isLogin: false
			});
		}

		if (mustLogin && method == 'cancel') {
			wx.showModal({
				title: '温馨提示',
				content: '此功能仅限注册用户',
				confirmText: '马上登录',
				cancelText: '取消',
				success(result) {
					if (result.confirm) {
						let url = pageHelper.fmtURLByPID('/pages/my/login/my_login') + '?retUrl=back';
						wx.navigateTo({ url });

					} else if (result.cancel) {

					}
				}
			});

			return false;
		}
		else if (mustLogin && method == 'back') {
			wx.showModal({
				title: '温馨提示',
				content: '此功能仅限注册用户',
				confirmText: '马上登录',
				cancelText: '返回',
				success(result) {
					if (result.confirm) {
						let url = pageHelper.fmtURLByPID('/pages/my/login/my_login');
						wx.redirectTo({ url });
					} else if (result.cancel) {
						let len = getCurrentPages().length;
						if (len == 1) {
							let url = pageHelper.fmtURLByPID('/pages/default/index/default_index');
							wx.reLaunch({ url });
						}
						else
							wx.navigateBack();

					}
				}
			});

			return false;
		}
		else if (mustLogin && method == 'back') {
			wx.showModal({
				title: '温馨提示',
				content: '此功能仅限注册用户',
				confirmText: '马上注册',
				cancelText: '返回',
				success(result) {
					if (result.confirm) {
						let url = pageHelper.fmtURLByPID('/pages/my/reg/my_reg');
						wx.reLaunch({ url });
					} else if (result.cancel) {
						wx.navigateBack();
					}
				}
			});

			return false;
		}

	}

	// 清除登录缓存
	static clearToken() {
		cacheHelper.remove(constants.CACHE_TOKEN);
	}


}



/** 表单校验    */
PassportBiz.CHECK_FORM = {
	name: 'formName|must|string|min:1|max:30|name=姓名',
	mobile: 'formMobile|must|len:11|name=手机',
	forms: 'formForms|array'
};


module.exports = PassportBiz;