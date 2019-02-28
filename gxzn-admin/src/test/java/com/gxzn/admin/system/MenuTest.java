package com.gxzn.admin.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxzn.admin.base.BaseJunit;
import com.gxzn.admin.modular.system.entity.Menu;
import com.gxzn.admin.modular.system.mapper.MenuMapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Stack;

/**
 * 菜单测试
 *
 * @author fengshuonan
 * @date 2017-06-13 21:23
 */
public class MenuTest extends BaseJunit {

    @Autowired
    MenuMapper menuMapper;

    /**
     * 初始化pcodes
     *
     * @author stylefeng
     * @Date 2017/6/13 21:24
     */
    @Test
    public void generatePcodes() {
        List<Menu> menus = menuMapper.selectList(null);
        for (Menu menu : menus) {
            if ("0".equals(menu.getPcode()) || null == menu.getPcode()) {
                menu.setPcodes("[0],");
            } else {
                StringBuffer sb = new StringBuffer();
                Menu parentMenu = getParentMenu(menu.getCode());
                sb.append("[0],");
                Stack<String> pcodes = new Stack<>();
                while (null != parentMenu.getPcode()) {
                    pcodes.push(parentMenu.getCode());
                    parentMenu = getParentMenu(parentMenu.getPcode());
                }
                int pcodeSize = pcodes.size();
                for (int i = 0; i < pcodeSize; i++) {
                    String code = pcodes.pop();
                    if (code.equals(menu.getCode())) {
                        continue;
                    }
                    sb.append("[" + code + "],");
                }

                menu.setPcodes(sb.toString());
            }
            menuMapper.updateById(menu);
        }
    }

    private Menu getParentMenu(String code) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("CODE", code);
        List<Menu> menus = menuMapper.selectList(wrapper);
        if (menus == null || menus.size() == 0) {
            return new Menu();
        } else {
            return menus.get(0);
        }
    }
}
