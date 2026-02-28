const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');
const validate = require('../../../../../../helper/validate.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		if (!AdminBiz.isAdmin(this, 'mgr')) return;
		if (!pageHelper.getOptions(this, options)) return;

		this._loadDetail();
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

	_loadDetail: async function () {
		if (!AdminBiz.isAdmin(this, 'mgr')) return;

		let id = this.data.id;
		if (!id) return; 
		let params = {
			id
		};
		let opt = {
			title: 'bar'
		};
		let mgr = await cloudHelper.callCloudData('/admin/mgr/detail', params, opt);
		if (!mgr) {
			this.setData({
				isLoad: null
			})
			return;
		};

		this.setData({
			isLoad: true,

			// 表单数据 
			formType: mgr.adminType,
			formName: mgr.adminName,
			formDesc: mgr.adminDesc,
			formPhone: mgr.adminPhone,

			formPassword: ''

		});
	},

	/** 
	 * 数据提交
	 */
	bindFormSubmit: async function () {
		if (!AdminBiz.isAdmin(this, 'mgr')) return;

		let data = this.data;

		// 数据校验 
		data = validate.check(data, AdminBiz.CHECK_FORM_MGR_EDIT, this);
		if (!data) return;

		try {
			let adminId = this.data.id;
			data.id = adminId;

			await cloudHelper.callCloudSumbit('admin/mgr/edit', data).then(res => {

				let callback = () => {
					// 更新列表页面数据
					let node = {
						'adminType': data.type,
						'adminName': data.name,
						'adminDesc': data.desc,
						'adminPhone': data.phone,
					}
					pageHelper.modifyPrevPageListNodeObject(adminId, node, 2, 'dataList', 'adminId');

					wx.navigateBack();
				}
				pageHelper.showSuccToast('修改成功', 1500, callback);
			});


		} catch (err) {
			console.log(err);
		}

	},
})