package com.gxzn.admin.modular.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gxzn.admin.core.common.constant.Const;
import com.gxzn.admin.core.common.constant.state.ManagerStatus;
import com.gxzn.admin.core.common.constant.state.UserExamine;
import com.gxzn.admin.core.common.constant.state.UserType;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.node.MenuNode;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import com.gxzn.admin.core.shiro.service.UserAuthService;
import com.gxzn.admin.core.util.ApiMenuFilter;
import com.gxzn.admin.modular.system.entity.User;
import com.gxzn.admin.modular.system.factory.UserFactory;
import com.gxzn.admin.modular.system.mapper.UserMapper;
import com.gxzn.admin.modular.system.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserAuthService userAuthService;

    /**
     * 添加用户
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addUser(UserDto user) {

        // 判断账号是否重复
        User theUser = this.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        String salt = ShiroKit.getRandomSalt(5);
        String password = ShiroKit.md5(user.getPassword(), salt);

        User createUser = UserFactory.createUser(user, password, salt);
        if (createUser.getType() == null) {
            createUser.setType(UserType.PLATEFORM.getCode());
        }
        if (UserType.PLATEFORM.getCode() == createUser.getType()) {// 若为平台用户
            createUser.setExamine(UserExamine.PASS.getCode());// 设置为审核通过
        } else {
            createUser.setExamine(UserExamine.NO_PASS.getCode());// 设置为审核不通过
        }
        this.save(createUser);
    }

    /**
     * 修改用户
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void editUser(UserDto user) {
        User oldUser = this.getById(user.getUserId());
        //如果登录用户权限是超管
        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
            this.updateById(UserFactory.editUser(user, oldUser));
        } else {
            //如果是修改自己的账号
            this.assertAuth(user.getUserId());
            ShiroUser shiroUser = ShiroKit.getUserNotNull();
            if (shiroUser.getId().equals(user.getUserId())) {
                this.updateById(UserFactory.editUser(user, oldUser));
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }

    /**
     * 删除用户
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteUser(Long userId) {

        // 不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        this.assertAuth(userId);
        this.setStatus(userId, ManagerStatus.DELETED.getCode());
    }

    /**
     * 修改用户状态
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int setStatus(Long userId, String status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        return this.baseMapper.updateById(user);
        // return this.baseMapper.setStatus(userId, status);
    }

    /**
     * 修改密码
     */
    public void changePwd(String oldPassword, String newPassword) {
        Long userId = ShiroKit.getUserNotNull().getId();
        User user = this.getById(userId);

        String oldMd5 = ShiroKit.md5(oldPassword, user.getSalt());

        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPassword, user.getSalt());
            user.setPassword(newMd5);
            this.updateById(user);
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 根据条件查询用户列表
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime,
            Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectUsers(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 设置用户的角色
     */
    public int setRoles(Long userId, String roleIds) {
        User user = new User();
        user.setUserId(userId);
        user.setRoleId(roleIds);
        return this.baseMapper.updateById(user);
        // return this.baseMapper.setRoles(userId, roleIds);
    }

    /**
     * 通过账号获取用户
     */
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    /**
     * 获取用户菜单列表
     */
    public List<MenuNode> getUserMenuNodes(List<Long> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);
            List<MenuNode> titles = MenuNode.buildTitle(menus);
            return ApiMenuFilter.build(titles);
        }

    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    public void assertAuth(Long userId) {
        if (ShiroKit.isAdmin()) {
            return;
        }
        List<Long> deptDataScope = ShiroKit.getDeptDataScope();
        User user = this.getById(userId);
        Long deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }

    }

    /**
     * 刷新当前登录用户的信息
     */
    public void refreshCurrentUser() {
        ShiroUser user = ShiroKit.getUserNotNull();
        Long id = user.getId();
        User currentUser = this.getById(id);
        ShiroUser shiroUser = userAuthService.shiroUser(currentUser);
        ShiroUser lastUser = ShiroKit.getUser();
        BeanUtil.copyProperties(shiroUser, lastUser);
    }

}
