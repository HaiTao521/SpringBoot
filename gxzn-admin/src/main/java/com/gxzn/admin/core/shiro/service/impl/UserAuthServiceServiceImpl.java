package com.gxzn.admin.core.shiro.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import com.gxzn.admin.core.shiro.ShiroUserExpand;
import com.gxzn.admin.core.shiro.service.UserAuthService;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.constant.state.ManagerStatus;
import com.gxzn.admin.core.common.constant.state.UserExamine;
import com.gxzn.admin.core.common.constant.state.UserType;
import com.gxzn.admin.modular.system.entity.User;
import com.gxzn.admin.modular.system.entity.UserExpand;
import com.gxzn.admin.modular.system.mapper.MenuMapper;
import com.gxzn.admin.modular.system.mapper.UserExpandMapper;
import com.gxzn.admin.modular.system.mapper.UserMapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.util.SpringContextHolder;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class UserAuthServiceServiceImpl implements UserAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

//    @Autowired
//    private UserService userService;

    @Autowired
    private UserExpandMapper userExpandMapper;

    public static UserAuthService me() {
        return SpringContextHolder.getBean(UserAuthService.class);
    }

    @Override
    public User user(String account) {

        User user = userMapper.getByAccount(account);

        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (!user.getStatus().equals(ManagerStatus.OK.getCode())) {
            throw new LockedAccountException();
        }
        return user;
    }

    @Override
    public ShiroUser shiroUser(User user) {

        ShiroUser shiroUser = ShiroKit.createShiroUser(user);

        // 用户角色数组
        Long[] roleArray = Convert.toLongArray(user.getRoleId());

        // 获取用户角色列表
        List<Long> roleList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        for (Long roleId : roleArray) {
            roleList.add(roleId);
            roleNameList.add(ConstantFactory.me().getSingleRoleName(Convert.toStr(roleId)));
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        // 获取用户拥有的菜单
//        shiroUser.setMenus(userService.getUserMenuNodes(roleList));

        // 判断是否为供应商,并且状态为待审核状态
        if (user.getType() == UserType.SUPPLIER.getCode() && user.getExamine() != UserExamine.NO_AUTH.getCode()) {
            UserExpand userExpand = userExpandMapper.selectById(user.getUserId());
            ShiroUserExpand shiroUserExpand = new ShiroUserExpand();
            BeanUtil.copyProperties(userExpand, shiroUserExpand);
            shiroUser.setShiroUserExpand(shiroUserExpand);
        }
        // 设置用户的资源编码
        List<String> resourcesCodes = menuMapper.getResCodesByRoleId(roleList);
        shiroUser.setResourcesCodes(resourcesCodes);

        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Long roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }

    @Override
    public String findRoleNameByRoleId(Long roleId) {
        return ConstantFactory.me().getSingleRoleTip(Convert.toStr(roleId));
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
