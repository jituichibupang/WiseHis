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
		agree: false,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		ProjectBiz.initPage(this);

		this.setData({
			isLoad: true,

			fields: projectSetting.USER_FIELDS,

			formAccount: '',
			formName: '',
			formPassword: '',
			formPassword2: '',
			formForms: []
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


	bindAgreeTap: function (e) {
		this.setData({ agree: !this.data.agree });
	},

	url: function (e) {
		pageHelper.url(e, this);
	},

	bindSubmitTap: async function (e) {
		if (!this.data.agree) {
			pageHelper.showModal('请先阅读并同意 《用户使用协议》', '温馨提示');
			return;
		}
		try {
			let data = this.data;

			// 数据校验 
			const USER_CHECK_FORM = {
				account: 'formAccount|must|string|name=用户名',
				name: 'formName|must|string|name=姓名',
				password: 'formPassword|must|string|min:6|max:30|name=密码',
				password2: 'formPassword2|must|string|min:6|max:30|name=密码确认',
				forms: 'formForms|array'
			};
			data = validate.check(data, USER_CHECK_FORM, this);
			if (!data) return;
			if (data.password != data.password2) {
				return pageHelper.showModal('两次输入的密码不一致');
			}

			let forms = this.selectComponent("#cmpt-form").getForms(true);
			if (!forms) return;
			data.forms = JSON.stringify(forms);
			data.obj = JSON.stringify(dataHelper.dbForms2Obj(forms));

			let opts = {
				title: '提交中'
			}
			await cloudHelper.callCloudSumbit('passport/register', data, opts).then(res => {
				if (res && helper.isDefined(res.data.token) && res.data.token) {

					PassportBiz.setToken(res.data.token, res.data.token.expire);

					let callback = () => {
						wx.reLaunch({ url: '../index/my_index' });
					}


					pageHelper.showSuccToast('注册成功', 1500, callback);
				}
			});
		} catch (err) {
			console.error(err);
		}
	}
})