package com.gxzn.admin.core.metadata;

import cn.stylefeng.roses.core.metadata.CustomMetaObjectHandler;
import com.gxzn.admin.core.shiro.ShiroKit;
import org.springframework.stereotype.Component;

/**
 * 字段填充器
 */
@Component
public class ProcurementGovMpFieldHandler extends CustomMetaObjectHandler {

    @Override
    protected Object getUserUniqueId() {
        try {

            return ShiroKit.getUser().getId();

        } catch (Exception e) {

            //如果获取不到当前用户就存空id
            return "";
        }
    }
}
