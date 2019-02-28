package com.gxzn.admin.modular.system.controller;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

import java.util.List;

import javax.validation.Valid;

import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.node.MenuNode;
import com.gxzn.admin.core.log.LogManager;
import com.gxzn.admin.core.log.factory.LogTaskFactory;
import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import com.gxzn.admin.modular.system.model.UserDto;
import com.gxzn.admin.modular.system.service.UserService;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;

/**
 * 登录控制器
 */
@Controller
public class LoginController extends BaseController {
    
    @Autowired
    private UserService userService;

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        // 获取当前用户角色列表
        ShiroUser user = ShiroKit.getUserNotNull();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

//        model.addAttribute("menus", user.getMenus());
        List<MenuNode> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);

        return "/index.html";
    }

    /**
     * 跳转到商家注册 Method: index
     * 
     * @return
     */
    @RequestMapping(value = "/toRegister", method = RequestMethod.GET)
    public String toRegister() {
        return "/register.html";
    }

    /**
     * 商家注册
     * Method: add
     * @date 2019年1月30日 上午11:35:47
     * @param user
     * @param result
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResponseData add(@Valid UserDto user, BindingResult result) {
    	if (user.getType() == null) {
    		user.setType(1);
		}
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.addUser(user);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali() {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        // 如果开启了记住我功能
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        // 执行shiro登录操作
        currentUser.login(token);

        // 登录成功，记录登录日志
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUserNotNull().getId(), getIp()));
        ShiroKit.getSubject().logout();
        deleteAllCookie();
        return REDIRECT + "/login";
    }
}
