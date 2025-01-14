const cloudHelper = require('../../../../../helper/cloud_helper.js');
const pageHelper = require('../../../../../helper/page_helper.js');
const dataHelper = require('../../../../../helper/data_helper.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');
const projectSetting = require('../../../public/project_setting.js');

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,

		joinForms: projectSetting.MEET_JOIN_FIELDS,

		forms: [],
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		ProjectBiz.initPage(this);
		if (!await PassportBiz.loginMustBackWin(this)) return;

		if (!pageHelper.getOptions(this, options)) return;

		let day = decodeURIComponent(options.day);
		let time = decodeURIComponent(options.time);
		this.setData({ day, time });

		this._loadDetail();



	},

	_loadDetail: async function () {
		let id = this.data.id;
		if (!id) return;


		let params = {
			meetId: id,
			day: this.data.day,
			time: this.data.time
		};
		let opt = {
			title: 'bar'
		};
		let meet = await cloudHelper.callCloudData('/meet/detail/for/join', params, opt);
		if (!meet) {
			this.setData({
				isLoad: null
			})
			return;
		}

		meet.myForms = JSON.parse(meet.myForms);
		this.setData({
			isLoad: true,
			meet,
		});

	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () { },

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
		this.setData({
			isLoad: false
		}, async () => {
			await this._loadDetail();
		})
		wx.stopPullDownRefresh();
	},



	url: function (e) {
		pageHelper.url(e, this);
	},

	onPageScroll: function (e) {
		// 回页首按钮
		pageHelper.showTopBtn(e, this);

	},

	bindCheckTap: async function (e) {
		this.selectComponent("#form-show").checkForms();
	},

	bindSubmitCmpt: async function (e) {
		let forms = e.detail;

		try {
			let opts = {
				title: '提交中'
			}
			let params = {
				meetId: this.data.id,
				day: this.data.day,
				time: this.data.time,
				forms: JSON.stringify(forms),
				obj: JSON.stringify(dataHelper.dbForms2Obj(forms))
			}
			await cloudHelper.callCloudSumbit('meet/join', params, opts).then(res => {

				let parent = pageHelper.getPrevPage(2);
				if (parent) parent._loadDetail();
				wx.showModal({
					title: '温馨提示',
					showCancel: false,
					content: '预约成功！',
					success() {
						let ck = () => {
							wx.navigateBack();
						}
						ck();
					}
				})
			})
		} catch (err) {
			console.log(err);
		};
	}

})