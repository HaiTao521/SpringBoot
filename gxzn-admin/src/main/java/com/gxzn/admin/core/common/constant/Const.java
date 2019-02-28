package com.gxzn.admin.core.common.constant;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * 系统常量
 */
public interface Const {

    /**
     * 系统默认的管理员密码
     */
    String DEFAULT_PWD = "111111";

    /**
     * 管理员角色的名字
     */
    String ADMIN_NAME = "administrator";

    /**
     * 管理员id
     */
    Long ADMIN_ID = 1L;

    /**
     * 超级管理员角色id
     */
    Long ADMIN_ROLE_ID = 1L;

    /**
     * 接口文档的菜单名
     */
    String API_MENU_NAME = "接口文档";

    /**
     * 不需要权限验证的资源表达式
     */
    List<String> NONE_PERMISSION_RES = CollectionUtil.newLinkedList("/assets/**", "/gunsApi/**", "/login",
            "/toRegister", "/mgr/register", "/global/sessionError", "/kaptcha", "/error", "/global/error","/images/**");

    /**
     * 供应商创建人ID
     */
    Long SUPPLIER_CREATE_USER_ID = -999L;

    /**
     * 临时供应商角色ID：供应商注册后会自动加入到此角色中
     */
    String SUPPLIER_TEMP_ROLE_ID = "3";

    /**
     * 供应商角色ID
     */
    String SUPPLIER_ROLE_ID = "2";

    /**
     * 供应商部门ID
     */
    Long SUPPLIER_DEPT_ID = 28L;

    /**
     * 商品上传文件夹名称
     */
    String UPLOAD_FOLDER_COMMODITY = "commodity";

    /**
     * 供应商上传文件名称
     */
    String UPLOAD_FOLDER_SUPPLIER = "supplier";
    
    /**
     * 物流管理签收单文件名称
     */
    String UPLOAD_FOLDER_ORDERCOMMODITY = "ordercommodity";
}
