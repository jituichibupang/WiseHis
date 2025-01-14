/**
 * Notes: 项目通用业务逻辑
 * Ver : CCMiniCloud Framework 2.0.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2022-05-22 07:48:00 
 */

const BaseBiz = require('../../../comm/biz/base_biz.js'); 
const projectSetting = require('../public/project_setting.js');
const PubilcBiz = require('../../../comm/biz/public_biz.js');
const PassportBiz = require('../../../comm/biz/passport_biz.js'); 

class ProjectBiz extends BaseBiz {

	static selectLocation(that) {
		let callback = function (res) {
			if (!res || !res.name || !res.address || !res.latitude || !res.longitude)
				return;

			let formAddress = res.address + '  ' + res.name;

			let formAddressGeo = {};
			formAddressGeo.name = res.name;
			formAddressGeo.address = res.address;
			formAddressGeo.latitude = res.latitude;
			formAddressGeo.longitude = res.longitude;
			that.setData({
				formAddressGeo,
				formAddress
			});
		}
		if (that.data.formAddressGeo && that.data.formAddressGeo.latitude > 0) {
			wx.chooseLocation({
				latitude: that.data.formAddressGeo.latitude,
				longitude: that.data.formAddressGeo.longitude,
				success: function (res) {
					callback(res);
				}
			})
		} else {
			wx.chooseLocation({
				success: function (res) {
					callback(res);
				},
				fail: function (err) {
					console.log(err);
				}
			})
		}
	}

	/**
	 * 页面初始化    
	 * @param {*} that  
	 */
	static initPage(that, { isSetNavColor = true } = {}) {    

		let skin = {};
		skin.NAV_BG = projectSetting.NAV_BG;
		skin.NAV_COLOR = projectSetting.NAV_COLOR;
		skin.PROJECT_COLOR = projectSetting.PROJECT_COLOR;
		
		PubilcBiz.initPageBase(that, { skin, isSetNavColor });
	}

 

}

module.exports = ProjectBiz;