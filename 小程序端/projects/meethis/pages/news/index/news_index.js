const pageHelper = require('../../../../../helper/page_helper.js');
const ProjectBiz = require('../../../biz/project_biz.js');
const NewsBiz = require('../../../biz/news_biz.js');

Page({

	data: {
		isLoad: false,

		_params: null,

		sortMenus: [],
		sortItems: [], 
	},

	/**
		* 生命周期函数--监听页面加载
		*/
	onLoad: async function (options) {
		ProjectBiz.initPage(this);


		if (options && options.id) {
			this.setData({
				isLoad: true,
				_params: {
					cateId: options.id,
				}
			});
			NewsBiz.setCateTitle();
		} else {
			this._getSearchMenu();
			this.setData({
				isLoad: true
			});
		}

	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () { },

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: async function () {

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

	bindCommListCmpt: function (e) {
		pageHelper.commListListener(this, e);
	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

	_getSearchMenu: function (cateList) {

		let sortItem1 = [];

		if (NewsBiz.getCateList().length > 1)
			sortItem1 = sortItem1.concat(NewsBiz.getCateList());

		let sortItems = [];
		let sortMenus = [
			{ label: '全部', type: 'cateId', value: '' },

			...sortItem1,
		];
		this.setData({
			sortItems,
			sortMenus
		})

	}


})