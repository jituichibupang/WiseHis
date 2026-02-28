const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		if (!AdminBiz.isAdmin(this)) return;

		//设置搜索菜单
		await this._getSearchMenu();
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: async function () { },

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

	bindDelTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let id = pageHelper.dataset(e, 'id');

		let params = {
			id
		}

		let callback = async () => {
			try {
				let opts = {
					title: '删除中'
				}
				await cloudHelper.callCloudSumbit('admin/user/del', params, opts).then(res => {

					pageHelper.delListNode(id, this.data.dataList.list, 'userId');
					this.data.dataList.total--;
					this.setData({
						dataList: this.data.dataList
					});
					pageHelper.showSuccToast('删除成功');
				});
			} catch (e) {
				console.log(e);
			}
		}
		pageHelper.showConfirm('确认删除？删除不可恢复', callback);

	},

	bindStatusTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let status = pageHelper.dataset(e, 'status');

		let idx = Number(pageHelper.dataset(e, 'idx'));

		let dataList = this.data.dataList;
		let id = dataList.list[idx].userId;

		let params = {
			id,
			status,
			reason: this.data.formReason
		}

		let cb = async () => {
			try {
				await cloudHelper.callCloudSumbit('admin/user/status', params).then(res => {

					let data2Name = 'dataList.list[' + idx + '].userStatus';
					this.setData({
						[data2Name]: status
					});

					pageHelper.showSuccToast('操作成功');
				});
			} catch (e) {
				console.log(e);
			}
		}

		pageHelper.showConfirm('确认执行此操作?', cb);
	},

	bindPwdTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let id = pageHelper.dataset(e, 'id');

		wx.showModal({
			title: '修改密码',
			placeholderText: '请填写新密码',
			editable: true,
			async success(res) {
				if (!res.confirm) return;
				let pwd = res.content;
				if (!pwd) return pageHelper.showConfirm('请输入新密码！');
				if (pwd.length < 6) return pageHelper.showConfirm('新密码不能小于6位！');
				if (pwd.length > 30) return pageHelper.showConfirm('新密码不能大于30位！');

				let params = {
					id,
					pwd, 
				}

				try {
					await cloudHelper.callCloudSumbit('admin/user/pwd', params).then(res => { 

						pageHelper.showSuccToast('修改成功');
					});
				} catch (e) {
					console.log(e);
				}


			}
		}) 
	},

	_getSearchMenu: async function () {

		let sortItems1 = [
			{ label: '排序', type: '', value: '' },
			{ label: '注册时间△', type: 'sort', value: 'ADD_TIME|asc' },
			{ label: '注册时间▽', type: 'sort', value: 'ADD_TIME|desc' },
		];
		let sortMenus = [
			{ label: '全部', type: '', value: '' },
			{ label: '正常', type: 'status', value: 1 },
			{ label: '禁用', type: 'status', value: 9 }

		]

		this.setData({
			search: '',
			sortItems: [sortItems1],
			sortMenus,
			isLoad: true
		})


	}

})