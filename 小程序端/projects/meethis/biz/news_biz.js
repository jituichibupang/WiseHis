/**
 * Notes: 资讯模块业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2020-11-14 07:48:00 
 */

const BaseBiz = require('../../../comm/biz/base_biz.js');
const pageHelper = require('../../../helper/page_helper.js');
const projectSetting = require('../public/project_setting.js');

class NewsBiz extends BaseBiz {

	/** 取得分类 */
	static getCateList() {
		let cateList = projectSetting.NEWS_CATE;

		let arr = [];
		for (let k = 0; k < cateList.length; k++) {
			arr.push({
				label: cateList[k].title,
				type: 'cateId',

				id: cateList[k].id,
				title: cateList[k].title,
				style: cateList[k].style,

				val: cateList[k].id, //for options
				value: cateList[k].id, //for list
			})
		}
		return arr;
	}


	static setCateTitle(id = null) {

		let curPage = pageHelper.getPrevPage(1);
		if (!curPage) return;

		let cateId = null;
		if (id) cateId = id;
		else if (curPage.options && curPage.options.id) {
			cateId = curPage.options.id;
		}
		let cateList = projectSetting.NEWS_CATE;
		for (let k = 0; k < cateList.length; k++) {
			if (cateList[k].id == cateId) {
				wx.setNavigationBarTitle({
					title: cateList[k].title
				});
				return;
			}
		}

	}

	/** 搜索菜单设置 */
	static async getSearchMenu() {
		let sortMenus = [{
			label: '全部',
			type: '',
			value: ''
		}];
		let sortMenusAfter = [{
			label: '最新',
			type: 'sort',
			value: 'new'
		},];
		let sortItems = [];

		sortMenus = sortMenus.concat(sortMenusAfter);

		return {
			sortItems,
			sortMenus
		}
	}
}

module.exports = NewsBiz;