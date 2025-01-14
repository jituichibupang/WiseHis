package com.cclinux.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Notes: application.yml配置恩家读取
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/3 11:14
 * @Ver: ccminicloud-framework 3.2.1
 */


@Component
public class AppConfig {

    // JWT秘钥
    public static String JWT_SECERT;

    @Value("${jwt.secret}")
    public void setJwtSecert(String jwtSecert) {
        JWT_SECERT = jwtSecert;
    }

    // JWT 管理员过期时间 秒
    public static Long JWT_ADMIN_EXPIRE;

    @Value("${jwt.adminExpire}")
    public void setJwtAdminExpire(Long jwtAdminExpire) {
        JWT_ADMIN_EXPIRE = jwtAdminExpire;
    }

    // JWT 用户过期时间 秒
    public static Long JWT_CUST_EXPIRE;

    @Value("${jwt.custExpire}")
    public void setJwtCustExpire(Long jwtCustomerExpire) {
        JWT_CUST_EXPIRE = jwtCustomerExpire;
    }

    // JWT 工作人员过期时间 秒
    public static Long JWT_WORK_EXPIRE;

    @Value("${jwt.workExpire}")
    public void setJwtWorkExpire(Long jwtWorkExpire) {
        JWT_WORK_EXPIRE = jwtWorkExpire;
    }


    // 服务器外部访问 domain URL
    public static String SERVER_URL;

    @Value("${serverUrl}")
    public void setServerUrl(String serverUrl) {
        SERVER_URL = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
    }


    // 服务器存储目录
    public static String UPLOAD_PATH;

    @Value("${upload.path}")
    public void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
    }

    // 服务器存储的url访问地址
    public static String UPLOAD_SERVER;

    @Value("${upload.server}")
    public void setUploadServer(String uploadServer) {
        UPLOAD_SERVER = uploadServer.endsWith("/") ? uploadServer : uploadServer + "/";
    }


    public static Boolean IS_DEMO;

    @Value("${isDemo}")
    public void setDemo(Boolean isDemo) {
        IS_DEMO = isDemo;
    }


}
