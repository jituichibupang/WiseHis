const pageHelper = require('../../../../../helper/page_helper.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const MeetBiz = require('../../../biz/meet_biz.js');
const PassportBiz = require('../../../../../comm/biz/passport_biz.js');

Page({
	/**
	 * 页面的初始数据
	 */
	data: {
		isLogin: true
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		ProjectBiz.initPage(this);

		this._getSearchMenu();
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
	onPullDownRefresh: function () {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

	url: async function (e) {
		pageHelper.url(e, this);
	},

	bindCommListCmpt: function (e) {
		pageHelper.commListListener(this, e);
	},

	/** 搜索菜单设置 */
	_getSearchMenu: function () {
		let sortMenus = [
			{ label: '全部', type: '', value: '' },
			{ label: '日期▽', type: 'sort', value: 't.MEET_JOIN_DAY|desc' },
			{ label: '日期△', type: 'sort', value: 't.MEET_JOIN_DAY|asc' },
		]

		this.setData({
			search: '',
			sortItems: [],
			sortMenus,
			isLoad: true
		});

	},
	bindCancelTap: async function (e) {
		if (!await PassportBiz.loginMustCancelWin(this)) return;

		let meetJoinId = pageHelper.dataset(e, 'id');

		let callback = () => {
			pageHelper.delListNode(meetJoinId, this.data.dataList.list, 'meetJoinId');
			this.data.dataList.total--;
			this.setData({
				dataList: this.data.dataList
			});
		}
		await MeetBiz.cancelMyMeetJoin(meetJoinId, callback);
	}
})