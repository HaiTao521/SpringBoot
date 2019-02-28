package com.gxzn.admin.modular.system.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.constant.state.MenuStatus;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.node.MenuNode;
import com.gxzn.admin.core.common.node.ZTreeNode;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.listener.ConfigListener;
import com.gxzn.admin.modular.system.entity.Menu;
import com.gxzn.admin.modular.system.mapper.MenuMapper;
import com.gxzn.admin.modular.system.model.MenuDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    @Resource
    private MenuMapper menuMapper;

    /**
     * 添加菜单
     */
    @Transactional
    public void addMenu(MenuDto menuDto) {

        if (ToolUtil.isOneEmpty(menuDto, menuDto.getCode(), menuDto.getName(), menuDto.getPid(), menuDto.getMenuFlag(),
                menuDto.getUrl())) {
            throw new RequestEmptyException();
        }

        // 判断是否已经存在该编号
        String existedMenuName = ConstantFactory.me().getMenuNameByCode(menuDto.getCode());
        if (ToolUtil.isNotEmpty(existedMenuName)) {
            throw new ServiceException(BizExceptionEnum.EXISTED_THE_MENU);
        }

        // 组装属性，设置父级菜单编号
        Menu resultMenu = this.menuSetPcode(menuDto);

        resultMenu.setStatus(MenuStatus.ENABLE.getCode());

        this.save(resultMenu);
    }

    /**
     * 删除菜单
     */
    @Transactional
    public void delMenu(Long menuId) {

        // 删除菜单
        this.menuMapper.deleteById(menuId);

        // 删除关联的relation
        this.menuMapper.deleteRelationByMenu(menuId);
    }

    /**
     * 删除菜单包含所有子菜单
     */
    @Transactional
    public void delMenuContainSubMenus(Long menuId) {

        Menu menu = menuMapper.selectById(menuId);

        // 删除当前菜单
        delMenu(menuId);

        // 删除所有子菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper = wrapper.like("PCODES", "%[" + menu.getCode() + "]%");
        List<Menu> menus = menuMapper.selectList(wrapper);
        for (Menu temp : menus) {
            delMenu(temp.getMenuId());
        }
    }

    /**
     * 根据条件查询菜单
     *
     * @return
     */
    public Page<Map<String, Object>> selectMenus(String condition, String level, Long menuId) {

        // 获取menuId的code
        String code = "";
        if (menuId != null && menuId != 0L) {
            Menu menu = this.getById(menuId);
            code = menu.getCode();
        }

        Page page = LayuiPageFactory.defaultPage();

        return this.baseMapper.selectMenus(page, condition, level, menuId, code);
    }

    /**
     * 根据条件查询菜单
     *
     * @return
     */
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

    /**
     * 获取菜单列表树
     *
     * @return
     */
    public List<ZTreeNode> menuTreeList() {
        return this.baseMapper.menuTreeList();
    }

    /**
     * 获取菜单列表树
     *
     * @return
     */
    public List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds) {
        return this.baseMapper.menuTreeListByMenuIds(menuIds);
    }

    /**
     * 删除menu关联的relation
     *
     * @param menuId
     * @return
     */
    public int deleteRelationByMenu(Long menuId) {
        return this.baseMapper.deleteRelationByMenu(menuId);
    }

    /**
     * 获取资源url通过角色id
     *
     * @param roleId
     * @return
     */
    public List<String> getResUrlsByRoleId(Long roleId) {
        return this.baseMapper.getResUrlsByRoleId(roleId);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     */
    public List<MenuNode> getMenusByRoleIds(List<Long> roleIds) {
        List<MenuNode> menus = this.baseMapper.getMenusByRoleIds(roleIds);
        // 给所有的菜单url加上ctxPath
        for (MenuNode menuItem : menus) {
            menuItem.setUrl(ConfigListener.getConf().get("contextPath") + menuItem.getUrl());
        }
        return menus;
    }

    /**
     * 根据code查询菜单
     *
     */
    public Menu selectByCode(String code) {
        Menu menu = new Menu();
        menu.setCode(code);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(menu);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据请求的父级菜单编号设置pcode和层级
     */
    public Menu menuSetPcode(MenuDto menuParam) {

        Menu resultMenu = new Menu();
        BeanUtil.copyProperties(menuParam, resultMenu);

        if (ToolUtil.isEmpty(menuParam.getPid()) || menuParam.getPid().equals(0L)) {
            resultMenu.setPcode("0");
            resultMenu.setPcodes("[0],");
            resultMenu.setLevels(1);
        } else {
            Long pid = menuParam.getPid();
            Menu pMenu = this.getById(pid);
            Integer pLevels = pMenu.getLevels();
            resultMenu.setPcode(pMenu.getCode());

            // 如果编号和父编号一致会导致无限递归
            if (menuParam.getCode().equals(menuParam.getPcode())) {
                throw new ServiceException(BizExceptionEnum.MENU_PCODE_COINCIDENCE);
            }

            resultMenu.setLevels(pLevels + 1);
            resultMenu.setPcodes(pMenu.getPcodes() + "[" + pMenu.getCode() + "],");
        }

        return resultMenu;
    }

}
