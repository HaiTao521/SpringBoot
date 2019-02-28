package com.gxzn.admin.modular.system.wrapper;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.core.util.ToolUtil;

import java.util.List;
import java.util.Map;

import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.util.Contrast;

/**
 * 日志列表的包装类
 */
public class LogWrapper extends BaseControllerWrapper {

    public LogWrapper(Map<String, Object> single) {
        super(single);
    }

    public LogWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String message = (String) map.get("message");

        Long userid = (Long) map.get("userId");
        map.put("userName", ConstantFactory.me().getUserNameById(Convert.toStr(userid)));

        //如果信息过长,则只截取前100位字符串
        if (ToolUtil.isNotEmpty(message) && message.length() >= 100) {
            String subMessage = message.substring(0, 100) + "...";
            map.put("message", subMessage);
        }

        //如果信息中包含分割符号;;;   则分割字符串返给前台
        if (ToolUtil.isNotEmpty(message) && message.contains(Contrast.separator)) {
            String[] msgs = message.split(Contrast.separator);
            map.put("regularMessage", msgs);
        } else {
            map.put("regularMessage", message);
        }
    }
}
