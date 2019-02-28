package com.gxzn.admin.modular.system.wrapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.modular.system.entity.Dict;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;

/**
 * 字典列表的包装
 */
public class DictWrapper extends BaseControllerWrapper {

    public DictWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        StringBuilder detail = new StringBuilder();
        Long id = Long.valueOf(map.get("dictId").toString());
        List<Dict> dicts = ConstantFactory.me().findInDict(Convert.toStr(id));
        if (dicts != null) {
            for (Dict dict : dicts) {
                detail.append(dict.getCode()).append(":").append(dict.getName()).append(",");
            }
            map.put("detail", StrUtil.removeSuffix(detail.toString(), ","));
        }
    }
}
