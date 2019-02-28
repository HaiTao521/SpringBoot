package com.gxzn.admin.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.core.common.annotion.BussinessLog;
import com.gxzn.admin.core.common.annotion.Permission;
import com.gxzn.admin.core.common.constant.dictmap.RoleDict;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.node.ZTreeNode;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.modular.system.entity.Role;
import com.gxzn.admin.modular.system.entity.User;
import com.gxzn.admin.modular.system.model.RoleDto;
import com.gxzn.admin.modular.system.service.RoleService;
import com.gxzn.admin.modular.system.service.UserService;
import com.gxzn.admin.modular.system.wrapper.RoleWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static String PREFIX = "/modular/system/role";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 跳转到角色列表页面
     *
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/role.html";
    }

    /**
     * 跳转到添加角色
     *
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "/role_add.html";
    }

    /**
     * 跳转到修改角色
     *
     */
    @Permission
    @RequestMapping(value = "/role_edit")
    public String roleEdit(@RequestParam Long roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = this.roleService.getById(roleId);
        LogObjectHolder.me().set(role);
        return PREFIX + "/role_edit.html";
    }

    /**
     * 跳转到权限分配
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:31 PM
     */
    @Permission
    @RequestMapping(value = "/role_assign/{roleId}")
    public String roleAssign(@PathVariable("roleId") Long roleId, Model model) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("roleId", roleId);
        return PREFIX + "/role_assign.html";
    }

    /**
     * 获取角色列表
     *
     */
    @Permission
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "roleName", required = false) String roleName) {
        Page<Map<String, Object>> roles = this.roleService.selectRoles(roleName);
        Page<Map<String, Object>> wrap = new RoleWrapper(roles).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 角色新增
     *
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    @Permission
    @ResponseBody
    public ResponseData add(Role role) {
        this.roleService.addRole(role);
        return SUCCESS_TIP;
    }

    /**
     * 角色修改
     *
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    @Permission
    @ResponseBody
    public ResponseData edit(RoleDto roleDto) {
        this.roleService.editRole(roleDto);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     *
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)
    @Permission
    @ResponseBody
    public ResponseData remove(@RequestParam Long roleId) {
        this.roleService.delRoleById(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 查看角色
     *
     */
    @RequestMapping(value = "/view/{roleId}")
    @ResponseBody
    public ResponseData view(@PathVariable Long roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Role role = this.roleService.getById(roleId);
        Map<String, Object> roleMap = BeanUtil.beanToMap(role);

        Long pid = role.getPid();
        String pName = ConstantFactory.me().getSingleRoleName(Convert.toStr(pid));
        roleMap.put("pName", pName);

        return ResponseData.success(roleMap);
    }

    /**
     * 配置权限
     *
     */
    @RequestMapping("/setAuthority")
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    @Permission
    @ResponseBody
    public ResponseData setAuthority(@RequestParam("roleId") Long roleId, @RequestParam("ids") String ids) {
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     *
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleService.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表，通过用户id
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable Long userId) {
        User theUser = this.userService.getById(userId);
        String roleId = theUser.getRoleId();
        if (ToolUtil.isEmpty(roleId)) {
            return this.roleService.roleTreeList();
        } else {
            String[] strArray = roleId.split(",");
            return this.roleService.roleTreeListByRoleId(strArray);
        }
    }

}
