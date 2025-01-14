const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');
const dataHelper = require('../../../../../../helper/data_helper.js');
const validate = require('../../../../../../helper/validate.js');
const AdminNewsBiz = require('../../../../biz/admin_news_biz.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		if (!AdminBiz.isAdmin(this)) return;
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

	model: function (e) {
		pageHelper.model(this, e);
	},

	_loadDetail: async function () {
		if (!AdminBiz.isAdmin(this)) return;

		let id = this.data.id;
		if (!id) return;

		if (!this.data.isLoad) this.setData(AdminNewsBiz.initFormData(id)); // 初始化表单数据

		let params = {
			id
		};
		let opt = {
			title: 'bar'
		};
		let news = await cloudHelper.callCloudData('admin/news/detail', params, opt);

		if (!news) {
			this.setData({
				isLoad: null
			})
			return;
		};

		this.setData({
			isLoad: true,

			// 表单数据  
			formCateId: news.newsCateId,
			formOrder: news.newsOrder,
			formTitle: news.newsTitle,
			formForms: JSON.parse(news.newsForms)

		});
	},

	/** 
	 * 数据提交
	 */
	bindFormSubmit: async function () {
		if (!AdminBiz.isAdmin(this)) return;

		// 数据校验
		let data = this.data;

		data = validate.check(data, AdminNewsBiz.CHECK_FORM, this);
		if (!data) return;

		let forms = this.selectComponent("#cmpt-form").getForms(true);
		if (!forms) return;
		data.forms = JSON.stringify(forms);
		data.obj = JSON.stringify(dataHelper.dbForms2Obj(forms));

		data.cateName = AdminNewsBiz.getCateName(data.cateId);

		try {
			let id = this.data.id;
			data.id = id;

			await cloudHelper.callCloudSumbit('admin/news/edit', data).then(res => {
				let callback = async () => {

					// 更新列表页面数据
					let node = {
						'newsTitle': data.title,
						'newsCateName': data.cateName,
						'newsOrder': data.order,
					}
					pageHelper.modifyPrevPageListNodeObject(id, node, 2, 'dataList', 'newsId');

					wx.navigateBack();

				}
				pageHelper.showSuccToast('修改成功', 2000, callback);
			});


		} catch (err) {
			console.log(err);
		}

	}, 

	url: function (e) {
		pageHelper.url(e, this);
	}, 

})