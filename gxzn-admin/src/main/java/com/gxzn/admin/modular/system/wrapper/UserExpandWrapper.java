package com.gxzn.admin.modular.system.wrapper;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 */
public class UserExpandWrapper extends BaseControllerWrapper {

    public UserExpandWrapper(Map<String, Object> single) {
        super(single);
    }

    public UserExpandWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public UserExpandWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public UserExpandWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((String) map.get("sex")));
        map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleId")));
        map.put("deptName", ConstantFactory.me().getDeptName(Convert.toStr(map.get("deptId"))));
        map.put("statusName", ConstantFactory.me().getStatusName((String) map.get("status")));
    }

}
