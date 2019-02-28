package com.gxzn.admin.core.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gxzn.admin.core.common.annotion.PermissionValidateType;
import com.gxzn.admin.core.listener.ConfigListener;
import com.gxzn.admin.core.shiro.service.PermissionCheckService;

import cn.hutool.core.collection.CollectionUtil;
import cn.stylefeng.roses.core.util.HttpContext;

/**
 * 权限自定义检查
 */
@Service
@Transactional(readOnly = true)
public class PermissionCheckServiceServiceImpl implements PermissionCheckService {

    @Override
    public boolean check(PermissionValidateType permissionValidateType, Object[] permissions) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return false;
        }
        ArrayList<Object> objects = CollectionUtil.newArrayList(permissions);
        if (permissionValidateType == PermissionValidateType.ROLE) {
            String join = CollectionUtil.join(objects, ",");
            if (ShiroKit.hasAnyRoles(join)) {
                return true;
            }
        } else if (permissionValidateType == PermissionValidateType.RES_CODE) {
            List<String> resourcesCodes = user.getResourcesCodes();
            return resourcesCodes != null && !resourcesCodes.isEmpty() && resourcesCodes.containsAll(objects);
        } else if (permissionValidateType == PermissionValidateType.URL) {
            return ShiroKit.getSubject().isPermittedAll((String[]) permissions);
        }

        return false;
    }

    @Override
    public boolean checkAll() {
        HttpServletRequest request = HttpContext.getRequest();
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return false;
        }
        String requestURI = request.getRequestURI().replaceFirst(ConfigListener.getConf().get("contextPath"), "");
        String[] str = requestURI.split("/");
        if (str.length > 3) {
            requestURI = "/" + str[1] + "/" + str[2];
        }
        if (ShiroKit.hasPermission(requestURI)) {
            return true;
        }
        return false;
    }

}
