package com.cclinux.framework.helper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.exception.AppException;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Notes: 图片文件处理
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/6 4:33
 * @Ver: ccminicloud-framework 3.2.1
 */


public class FileHelper {

    // form里的图片提取的正则表达式
    static final String REG_FORM_PIC = "\\[pic\\](.*?)\\\"";


    // 保存上传的图片文件，返回URL
    public static String saveUpLoadFile(MultipartFile file, String pid) {

        if (file.isEmpty()) throw new AppException("上传文件不能为空");

        // 生成文件名
        String fileName = file.getOriginalFilename();
        String extend = fileName.substring(fileName.lastIndexOf("."), fileName.length());

        String savePath = getFileSavePath(pid, extend, "pic");
        try {
            FileUtil.writeBytes(file.getBytes(), savePath);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new AppException("文件上传异常，请稍后再试");
        }

        return getFileSaveURL(savePath);
    }

    // 保存自己内部生成的图片文件，返回数据库保存的相对路径
    public static String saveLocalFile(String pid, byte[] data, String fileName) {

        String savePath = getFileSavePath(pid, "jpg", "qr");

        try {
            FileUtil.writeBytes(data, savePath);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new AppException("文件保存异常，请稍后再试");
        }

        return getFileSaveURL(savePath);

    }


    private static String getFileRelative(String pid, String ext, String prefix) {
        if (!pid.endsWith("/")) pid += '/';

        String day = TimeHelper.getNowTime("yyyyMMdd") + "/";

        ext = ext.toLowerCase();
        if (!ext.startsWith(".")) ext = "." + ext;

        if (!prefix.endsWith("_")) prefix += "_";

        String fileName = prefix + TimeHelper.getNowTime("") + RandomUtil.randomNumbers(8) + ext;

        return pid + day + fileName;
    }

    /**
     * 文件保存的服务器路径
     */
    public static String getFileSavePath(String pid, String ext, String prefix) {
        return AppConfig.UPLOAD_PATH + getFileRelative(pid, ext, prefix);
    }

    /**
     * 文件保存的URL，原始生成
     */
    public static String getFileSaveURL(String pid, String ext, String prefix) {
        return AppConfig.UPLOAD_SERVER + getFileRelative(pid, ext, prefix);
    }

    /**
     * 文件保存的URL,根据保存路径生成
     */
    public static String getFileSaveURL(String savePath) {
        return StrUtil.replace(savePath, AppConfig.UPLOAD_PATH, AppConfig.UPLOAD_SERVER);
    }

}
