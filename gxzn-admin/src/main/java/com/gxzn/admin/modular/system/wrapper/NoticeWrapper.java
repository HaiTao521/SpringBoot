package com.gxzn.admin.modular.system.wrapper;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 部门列表的包装
 */
public class NoticeWrapper extends BaseControllerWrapper {

    public NoticeWrapper(Map<String, Object> single) {
        super(single);
    }

    public NoticeWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public NoticeWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public NoticeWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long creater = (Long) map.get("createUser");
        map.put("createrName", ConstantFactory.me().getUserNameById(Convert.toStr(creater)));
    }
}
