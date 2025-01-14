const cloudHelper = require('../../../../../helper/cloud_helper.js');
const pageHelper = require('../../../../../helper/page_helper.js');
const dataHelper = require('../../../../../helper/data_helper.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,


		tabCur: 0,
		mainCur: 0,
		verticalNavTop: 0,

		showMind: true,
		showTime: false,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		ProjectBiz.initPage(this);
		if (!pageHelper.getOptions(this, options)) return;

		this._loadDetail();
	},

	_loadDetail: async function () {
		let id = this.data.id;
		if (!id) return;

		let params = {
			id,
		};
		let opt = {
			title: 'bar'
		};
		let meet = await cloudHelper.callCloudData('meet/view', params, opt);
		if (!meet) {
			this.setData({
				isLoad: null
			})
			return;
		}
		meet.meetObj = JSON.parse(meet.meetObj);
		let times = dataHelper.getArrByKey(meet.meetObj.time, 'title');

		this.setData({
			isLoad: true,
			meet,
			times
		});
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () { },

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () { },

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () { },

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () { },

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
	onReachBottom: function () { },

	bindJoinTap: async function (e) {
		if (!await PassportBiz.loginMustCancelWin(this)) return;

		let dayidx = pageHelper.dataset(e, 'dayidx');
		let timeidx = pageHelper.dataset(e, 'timeidx');

		let day = encodeURIComponent(this.data.meet.meetDays[dayidx].label);
		let time = encodeURIComponent(this.data.times[timeidx]);

		wx.navigateTo({
			url: '../join/meet_join?id=' + this.data.id + '&day=' + day + '&time=' + time,
		});
	},

	bindTabSelectTap: function (e) {
		let idx = pageHelper.dataset(e, 'idx');
		this.setData({
			tabCur: idx,
			mainCur: idx,
			verticalNavTop: (idx - 1) * 50
		})
	},

	bindShowMindTap: function (e) {
		this.setData({
			showMind: true,
			showTime: false
		});
	},

	bindShowTimeTap: function (e) {
		this.setData({
			showMind: false,
			showTime: true
		});
	},

	url: function (e) {
		pageHelper.url(e, this);
	},

	onPageScroll: function (e) {
		// 回页首按钮
		pageHelper.showTopBtn(e, this);

	},

	bindTopTap: function () {
		wx.pageScrollTo({
			scrollTop: 0
		})
	},

	bindVerticalMainScroll: function (e) {
		if (!this.data.isLoad) return;

		let list = this.data.meet.meetDays;
		let tabHeight = 0;

		for (let i = 0; i < list.length; i++) {
			let view = wx.createSelectorQuery().in(this).select("#main-" + i);
			view.fields({
				size: true
			}, data => {
				list[i].top = tabHeight;
				tabHeight = tabHeight + data.height;
				list[i].bottom = tabHeight;
			}).exec();
		}

		let scrollTop = e.detail.scrollTop + 20; // + i*0.5; //TODO
		for (let i = 0; i < list.length; i++) {

			if (scrollTop > list[i].top && scrollTop < list[i].bottom) {

				this.setData({
					verticalNavTop: (i - 1) * 50,
					tabCur: i
				})
				return false;
			}
		}
	},

	onShareAppMessage: function (res) {
		return {
			title: this.data.meet.meetTitle,
			imageUrl: this.data.meet.meetObj.cover[0]
		}
	}
})