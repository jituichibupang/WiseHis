const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');
const fileHelper = require('../../../../../../helper/file_helper.js');
const projectSetting = require('../../../../public/project_setting.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		url: '',
		time: '',

		isLoad: true,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		if (!AdminBiz.isAdmin(this)) return;

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
		wx.stopPullDownRefresh();
	},

	bindOpenTap: function (e) {
		fileHelper.openDoc('用户数据', this.data.url);
	},

	url: async function (e) {
		pageHelper.url(e, this);
	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	bindExportTap: async function (e) {
		try {
			let options = {
				title: '数据生成中'
			}

			let params = {
				fmt: JSON.stringify(projectSetting.USER_FIELDS)
			}

			await cloudHelper.callCloudSumbit('admin/user/export', params, options).then(res => {
				this.setData({ ...res.data });
				pageHelper.showModal('数据文件生成成功(' + res.data.total + '条记录), 请点击「直接打开」按钮或者复制文件地址下载');

			});
		} catch (err) {
			console.log(err);
			pageHelper.showNoneToast('导出失败，请重试');
		}

	},

})