const pageHelper = require('../../../../../helper/page_helper.js');
const helper = require('../../../../../helper/helper.js');
const cloudHelper = require('../../../../../helper/cloud_helper.js');
const dataHelper = require('../../../../../helper/data_helper.js');
const validate = require('../../../../../helper/validate.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const projectSetting = require('../../../public/project_setting.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: true,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		ProjectBiz.initPage(this);

		this.setData({
			isLoad: true,
			formOldPassword: '',
			formNewPassword: '',
			formNewPassword2: ''
		});

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
	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},


	url: function (e) {
		pageHelper.url(e, this);
	},

	bindSubmitTap: async function (e) {

		try {
			let data = this.data;

			// 数据校验 
			const USER_CHECK_FORM = {
				oldPassword: 'formOldPassword|must|string|min:6|max:30|name=旧密码',
				newPassword: 'formNewPassword|must|string|min:6|max:30|name=新密码',
				newPassword2: 'formNewPassword2|must|string|min:6|max:30|name=新密码确认',
			};
			data = validate.check(data, USER_CHECK_FORM, this);
			if (!data) return;
			if (data.newPassword != data.newPassword2) {
				return pageHelper.showModal('两次输入的新密码不一致');
			}

			let opts = {
				title: '提交中'
			}
			await cloudHelper.callCloudSumbit('passport/pwd', data, opts).then(res => {
				let cb = () => {
					wx.navigateBack();
				}
				pageHelper.showSuccToast('修改成功', 1500, cb);

			});
		} catch (err) {
			console.error(err);
		}
	}
})