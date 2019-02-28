package com.gxzn.admin.core.util;

import com.gxzn.admin.core.common.constant.Const;
import com.gxzn.admin.core.listener.ConfigListener;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.core.util.ToolUtil;

/**
 * 获取默认图片地址
 */
public class DefaultImages {

    /**
     * 默认的登录页面背景
     */
    public static String loginBg() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/login-register.jpg";
    }

    /**
     * 默认的用户图片的base64编码
     *
     */
    public static String defaultAvatarUrl() {
        return ConfigListener.getConf().get("contextPath") + "/system/previewAvatar";
    }

    /**
     * 默认的404错误页面背景
     */
    public static String error404() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/error-bg.jpg";
    }

    /**
     * 获取供应商上传图像URL前缀
     */
    public static String getSupplierUploadImagePreUrl() {
        return ConfigListener.getConf().get("contextPath") + "/" + Const.UPLOAD_FOLDER_SUPPLIER + StrUtil.SLASH;
    }

    /**
     * 获取商品上传图像URL前缀
     */
    public static String getCommodityUploadImagePreUrl() {
        return ConfigListener.getConf().get("contextPath") + "/" + Const.UPLOAD_FOLDER_COMMODITY + StrUtil.SLASH;
    }

    /**
     * 获取缩率图
     * 
     * @param imageName
     * @return
     */
    public static String getThumbnailsImageName(String imageName) {
        if (ToolUtil.isEmpty(imageName)) {
            return StrUtil.EMPTY;
        }
        String[] split = StrUtil.split(imageName, StrUtil.DOT);
        String pre = split[0];
        String end = split[1];
        return pre + "-small" + StrUtil.DOT + end;
    }
}
