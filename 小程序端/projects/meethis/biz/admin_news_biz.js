/**
 * Notes: 资讯后台管理模块业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2020-11-14 07:48:00 
 */

const BaseBiz = require('../../../comm/biz/base_biz.js');
const NewsBiz = require('./news_biz.js');
const projectSetting = require('../public/project_setting.js');

class AdminNewsBiz extends BaseBiz {


	/** 表单初始化相关数据 */
	static initFormData(id = '') {
		let cateIdOptions = NewsBiz.getCateList();

		return {
			id,

			contentDesc: '',

			// 分类
			cateIdOptions,

			fields: projectSetting.NEWS_FIELDS,

			// 表单数据  
			formOrder: 9999,
			formTitle: '',  
			formCateId: (cateIdOptions.length == 1) ? cateIdOptions[0].val : '',
			formForms: [],
		 
		}

	}

	static getCateName(cateId) {
		let cateList = projectSetting.NEWS_CATE;

		for (let k = 0; k < cateList.length; k++) {
			if (cateList[k].id == cateId) return cateList[k].title;
		}
		return '';
	}

}


/** 表单校验  本地 */
AdminNewsBiz.CHECK_FORM = {
	title: 'formTitle|must|string|min:4|max:50|name=标题',
	cateId: 'formCateId|must|long|name=分类',
	order: 'formOrder|must|long|min:0|max:9999|name=排序号', 
};


module.exports = AdminNewsBiz;