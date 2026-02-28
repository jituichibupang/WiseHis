const pageHelper = require('../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../helper/cloud_helper.js');
const dataHelper = require('../../../../../helper/data_helper.js');
const validate = require('../../../../../helper/validate.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const projectSetting = require('../../../public/project_setting.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');
const constants = require('../../../../../comm/constants.js');

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,
		isEdit: true,

	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		ProjectBiz.initPage(this);
		if (!PassportBiz.loginMustBackWin(this)) return;

		await this._loadDetail();
	},

	_loadDetail: async function (e) {

		let opts = {
			title: 'bar'
		}
		let user = await cloudHelper.callCloudData('passport/my/detail', {}, opts);
		if (!user) {
			PassportBiz.clearToken();
			return wx.reLaunch({
				url: '../index/my_index',
			});
		}

		this.setData({
			isLoad: true,
			isEdit: true,


			fields: projectSetting.USER_FIELDS,

			formAccount: user.userAccount,
			formName: user.userName,
			formForms: JSON.parse(user.userForms)
		})
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

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: async function () {
		await this._loadDetail();
		wx.stopPullDownRefresh();
	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	bindGetPhoneNumber: async function (e) {
		await PassportBiz.getPhone(e, this);
	},


	bindSubmitTap: async function (e) {
		try {
			let data = this.data;
			// 数据校验 
			const CHECK_FORM = {
				account: 'formAccount|must|string|name=用户名',
				name: 'formName|must|string|name=姓名',
				forms: 'formForms|array'
			};
			data = validate.check(data, CHECK_FORM, this);
			if (!data) return;

			let forms = this.selectComponent("#cmpt-form").getForms(true);
			if (!forms) return;
			data.forms = JSON.stringify(forms);
			data.obj = JSON.stringify(dataHelper.dbForms2Obj(forms));

			let opts = {
				title: '提交中'
			}
			await cloudHelper.callCloudSumbit('passport/my/edit', data, opts).then(res => {
				let callback = () => {
					let token = PassportBiz.getToken();
					token.account = data.account;
					token.name = data.name;
					wx.setStorageSync(constants.CACHE_TOKEN, token);
					wx.reLaunch({ url: '../index/my_index' });
				}
				pageHelper.showSuccToast('修改成功', 1000, callback);
			});
		} catch (err) {
			console.error(err);
		}
	}
})