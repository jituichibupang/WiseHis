const pageHelper = require('../../../../../helper/page_helper.js');
const helper = require('../../../../../helper/helper.js');
const cloudHelper = require('../../../../../helper/cloud_helper.js');
const validate = require('../../../../../helper/validate.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {

	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad(options) {
		ProjectBiz.initPage(this);
		PassportBiz.clearToken();
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady() {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow() {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide() {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload() {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh() {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom() {

	},

	url: function (e) {
		pageHelper.url(e, this);
	},

	bindSubmitTap: async function (e) {

		try {
			let data = this.data;

			// 数据校验 
			const CHECK_FORM = {
				account: 'formAccount|must|string|name=用户名',
				password: 'formPassword|must|string|min:6|max:30|name=密码'
			};

			data = validate.check(data, CHECK_FORM, this);
			if (!data) return;


			let opts = {
				title: '登录中'
			}
			await cloudHelper.callCloudSumbit('passport/login', data, opts).then(res => {
				if (res && helper.isDefined(res.data.token) && res.data.token) {

					PassportBiz.setToken(res.data.token, res.data.token.expire);

					let callback = () => {
						wx.reLaunch({ url: '../index/my_index' });
					}


					pageHelper.showSuccToast('登录成功', 1500, callback);
				}
			});
		} catch (err) {
			console.error(err);
		}
	}
})