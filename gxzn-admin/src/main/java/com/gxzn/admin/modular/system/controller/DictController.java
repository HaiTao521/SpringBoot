package com.gxzn.admin.modular.system.controller;

import java.util.Map;

import com.gxzn.admin.core.common.annotion.BussinessLog;
import com.gxzn.admin.core.common.annotion.Permission;
import com.gxzn.admin.core.common.constant.dictmap.DictMap;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.modular.system.model.DictDto;
import com.gxzn.admin.modular.system.service.DictService;
import com.gxzn.admin.modular.system.wrapper.DictWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;

/**
 * 字典控制器
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/modular/system/dict/";

    @Autowired
    private DictService dictService;

    /**
     * 跳转到字典管理首页
     *
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典类型
     *
     */
    @RequestMapping("/dict_add_type")
    public String deptAddType() {
        return PREFIX + "dict_add_type.html";
    }

    /**
     * 跳转到添加字典条目
     *
     */
    @RequestMapping("/dict_add_item")
    public String deptAddItem(@RequestParam("dictId") Long dictId, Model model) {
        model.addAttribute("dictTypeId", dictId);
        model.addAttribute("dictTypeName", ConstantFactory.me().getDictName(Convert.toStr(dictId)));
        return PREFIX + "dict_add_item.html";
    }

    /**
     * 新增字典
     *
     */
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public ResponseData add(DictDto dictDto) {
        this.dictService.addDict(dictDto);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     *
     */
    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.dictService.list(condition);
        Page<Map<String, Object>> warpper = new DictWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(warpper);
    }

    /**
     * 删除字典记录
     */
    @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public ResponseData delete(@RequestParam Long dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(ConstantFactory.me().getDictName(Convert.toStr(dictId)));

        this.dictService.delteDict(dictId);

        return SUCCESS_TIP;
    }

}
