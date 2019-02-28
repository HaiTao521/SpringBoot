package com.gxzn.admin.core.shiro;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 */
@Data
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键ID
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 角色集
     */
    private List<Long> roleList;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色名称集
     */
    private List<String> roleNames;

//    /**
//     * 用户所能看到的菜单列表
//     */
//    private List<MenuNode> menus;

    /**
     * 类型（0-管理员，1-供应商）
     */
    private Integer type;

    /**
     * 供应商审核状态
     */
    private Integer examine;

    /**
     * 用户拓展信息，主要指供应商信息
     */
    private ShiroUserExpand shiroUserExpand;

    /**
     * 资源名称集合
     */
    private List<String> resourcesCodes;

}
