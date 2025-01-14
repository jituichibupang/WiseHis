package com.cclinux.projects.meethis.service.cust;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.JWTHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meethis.mapper.UserMapper;
import com.cclinux.projects.meethis.model.UserModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("MeetHisPassportService")
public class PassportService extends BaseMyCustService {

    @Resource(name = "MeetHisUserMapper")
    private UserMapper userMapper;

    /** 用户注册 */
    public void register(String account, String name, String password, String forms, String obj) {
        // 判断是否存在
        Where<UserModel> where = new Where<>();
        where.eq("USER_ACCOUNT", account);
        if (userMapper.exists(where))
            throw new AppException("该用户名已经存在，请更换！");

        UserModel user = new UserModel();
        user.setUserAccount(account);
        user.setUserName(name);
        user.setUserPassword(DigestUtil.md5Hex(password));
        user.setUserStatus(UserModel.STATUS.NORMAL);
        user.setUserForms(forms);
        user.setUserObj(obj);

        userMapper.add(user);

    }

    /** 修改个人资料 */
    public void edit(long userId, String account, String name, String forms, String obj) {
        // 判断是否存在
        Where<UserModel> where = new Where<>();
        where.eq("USER_ACCOUNT", account);
        where.ne("USER_ID", userId);
        if (userMapper.exists(where))
            throw new AppException("该用户名已经存在，请更换！");

        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", userId);
        uw.set("USER_ACCOUNT", account);
        uw.set("USER_NAME", name);
        uw.set("USER_FORMS", forms);
        uw.set("USER_OBJ", obj);

        userMapper.edit(uw);

    }

    /** 修改密码 */
    public void pwd(long userId, String oldPassword, String newPassword) {
        // 判断是否存在
        Where<UserModel> where = new Where<>();
        where.eq("USER_PASSWORD", DigestUtil.md5Hex(oldPassword));
        where.eq("USER_ID", userId);
        if (!userMapper.exists(where))
            throw new AppException("旧密码错误，请重新输入");

        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", userId);
        uw.set("USER_PASSWORD", DigestUtil.md5Hex(newPassword));

        userMapper.edit(uw);

    }

    /** 用户登录 */
    public Map<String, Object> login(String account, String password) {
        Where<UserModel> where = new Where<>();
        where.eq("USER_ACCOUNT", account);
        where.eq("USER_PASSWORD", DigestUtil.md5Hex(password));
        UserModel user = userMapper.getOne(where);
        if (ObjectUtil.isNull(user))
            throw new AppException("用户名或者密码错误");


		  if (user.getUserStatus() == UserModel.STATUS.FORBID) throw new AppException("用户已禁用");

        //  生成TOKEN
        String token = JWTHelper.genToken(user.getUserId(), user.getUserName(), "cust", AppConfig.JWT_CUST_EXPIRE);

        Map<String, Object> ret = new HashMap<>();
        ret.put("account", user.getUserAccount());
        ret.put("id", user.getUserId());
        ret.put("name", user.getUserName());
        ret.put("token", token);
        ret.put("expire", AppConfig.JWT_CUST_EXPIRE);

        if (user.getUserLoginTime() == 0)
            ret.put("last", "尚未登录");
        else
            ret.put("last", TimeHelper.timestamp2Time(user.getUserLoginTime()));


        user.setUserLoginTime(user.getUserLoginTime() + 1);
        user.setUserLoginTime(TimeHelper.timestamp());
        userMapper.update(user, where);


        return ret;
    }

    public Map<String, Object> getMyDetail(long userId) {
        return userMapper.getOneMap(userId, "USER_NAME,USER_ACCOUNT,USER_FORMS,USER_OBJ");
    }
}




