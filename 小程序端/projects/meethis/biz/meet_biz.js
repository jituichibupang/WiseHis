/**
 * Notes: 预约模块业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2024-06-24 07:48:00 
 */

const BaseBiz = require('../../../comm/biz/base_biz.js');
const pageHelper = require('../../../helper/page_helper.js');
const cloudHelper = require('../../../helper/cloud_helper.js');
const projectSetting = require('../public/project_setting.js');

class MeetBiz extends BaseBiz {

	static getCateName(cateId) {
		let cateList = projectSetting.MEET_CATE;

		for (let k = 0; k < cateList.length; k++) {
			if (cateList[k].id == cateId) {
				return cateList[k].title;
			}
		}
		return '';
	}

	static getCateList() {

		let cateList = projectSetting.MEET_CATE;

		let arr = [];
		for (let k = 0; k < cateList.length; k++) {
			arr.push({
				label: cateList[k].title,
				type: 'cateId',
				val: cateList[k].id, //for options form
				value: cateList[k].id, //for list menu
			})
		}

		return arr;
	}

	static setCateTitle() {

		let curPage = pageHelper.getPrevPage(1);
		if (!curPage) return;

		let cateId = null;
		if (curPage.options && curPage.options.id) {
			cateId = curPage.options.id;
		}
		let cateList = projectSetting.MEET_CATE;
		for (let k = 0; k < cateList.length; k++) {
			if (cateList[k].id == cateId) {
				wx.setNavigationBarTitle({
					title: cateList[k].title
				});
				return;
			}
		}

	}
 
	static async cancelMyMeetJoin(meetJoinId, callback) {
		let cb = async () => {
			try {
				let params = {
					meetJoinId
				}
				let opts = {
					title: '取消中'
				}

				await cloudHelper.callCloudSumbit('meet/my/join/cancel', params, opts).then(res => {
					pageHelper.showSuccToast('已取消', 1500, callback);
				});
			} catch (err) {
				console.log(err);
			}
		}

		pageHelper.showConfirm('确认取消该预约?', cb);
	}
 
}

module.exports = MeetBiz;