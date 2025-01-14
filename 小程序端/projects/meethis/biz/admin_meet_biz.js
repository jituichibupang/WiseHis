/**
 * Notes: 预约后台管理模块业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2024-06-24 07:48:00 
 */

const BaseBiz = require('../../../comm/biz/base_biz.js');
const MeetBiz = require('./meet_biz.js');
const projectSetting = require('../public/project_setting.js'); 

class AdminMeetBiz extends BaseBiz {
	static initFormData(id = '') {
		let cateIdOptions = MeetBiz.getCateList();

		return {
			id,

			cateIdOptions,
			fields: projectSetting.MEET_FIELDS,

			formTitle: '',
			formCateId: (cateIdOptions.length == 1) ? cateIdOptions[0].val : '',
			formOrder: 9999,

			formMaxCnt: 50,  
			formForms: [],
 
		}

	}

	
}

AdminMeetBiz.CHECK_FORM = {
	title: 'formTitle|must|string|min:2|max:50|name=标题',
	cateId: 'formCateId|must|id|name=分类',
	order: 'formOrder|must|int|min:0|max:9999|name=排序号',

	maxCnt: 'formMaxCnt|must|int|name=每时段人数上限', 
 
};

module.exports = AdminMeetBiz;