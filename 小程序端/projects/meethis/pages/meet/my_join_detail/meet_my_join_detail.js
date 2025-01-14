const pageHelper = require('../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../helper/cloud_helper.js');
const qrcodeLib = require('../../../../../lib/tools/qrcode_lib.js');
const ProjectBiz = require('../../../biz/project_biz.js'); 

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,

		isShowHome: false,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		ProjectBiz.initPage(this);
		if (!pageHelper.getOptions(this, options)) return;
		this._loadDetail();

		if (options && options.flag == 'home') {
			this.setData({
				isShowHome: true
			});
		}
	},

	_loadDetail: async function (e) {
		let id = this.data.id;
		if (!id) return;

		let params = {
			meetJoinId: id
		}
		let opts = {
			title: 'bar'
		}
		try {
			let meetJoin = await cloudHelper.callCloudData('meet/my/join/detail', params, opts);
			if (!meetJoin) {
				this.setData({
					isLoad: null
				})
				return;
			}

			let qrImageData = qrcodeLib.drawImg('meet=' + meetJoin.meetJoinCode, {
				typeNumber: 1,
				errorCorrectLevel: 'L',
				size: 100
			});

			meetJoin.meetJoinForms = JSON.parse(meetJoin.meetJoinForms);
			this.setData({
				isLoad: true,
				meetJoin,
				qrImageData
			});
		} catch (err) {
			console.error(err);
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

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: async function () {
		await this._loadDetail();
		wx.stopPullDownRefresh();
	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},
 

	url: function (e) {
		pageHelper.url(e, this);
	},


	bindCalendarTap: function (e) {
		let meetJoin = this.data.meetJoin;
		let title = meetJoin.meet.meetTitle;

		let startTime = meetJoin.meet.meetStart / 1000;
		let endTime = meetJoin.meet.meetEnd / 1000;

		pageHelper.addPhoneCalendar(title, startTime, endTime);
	}
})