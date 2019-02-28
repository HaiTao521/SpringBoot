package com.gxzn.admin.modular.system.mapper;

import java.util.List;
import java.util.Map;

import com.gxzn.admin.core.common.node.MenuNode;
import com.gxzn.admin.core.common.node.ZTreeNode;
import com.gxzn.admin.modular.system.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    Page<Map<String, Object>> selectMenus(@Param("page") Page page, @Param("condition") String condition, @Param("level") String level, @Param("menuId") Long menuId, @Param("code") String code);

    /**
     * 根据条件查询菜单
     *
     * @return
     */
    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取菜单列表树
     *
     * @return
     */
    List<ZTreeNode> menuTreeList();

    /**
     * 获取菜单列表树
     *
     * @return
     */
    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    /**
     * 删除menu关联的relation
     *
     * @param menuId
     * @return
     */
    int deleteRelationByMenu(Long menuId);

    /**
     * 获取资源url通过角色id
     *
     * @param roleId
     * @return
     */
    List<String> getResUrlsByRoleId(Long roleId);

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     */
    List<MenuNode> getMenusByRoleIds(List<Long> roleIds);
    
    /**
     * 根据角色获取资源编码
     * @param roleIds
     * @return
     */
    List<String> getResCodesByRoleId(List<Long>  roleIds);

}
