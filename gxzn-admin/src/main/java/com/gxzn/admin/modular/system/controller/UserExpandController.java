package com.gxzn.admin.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.config.properties.ProcurementGovProperties;
import com.gxzn.admin.core.common.annotion.BussinessLog;
import com.gxzn.admin.core.common.annotion.Permission;
import com.gxzn.admin.core.common.constant.Const;
import com.gxzn.admin.core.common.constant.dictmap.UserDict;
import com.gxzn.admin.core.common.constant.dictmap.UserExpendDict;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.constant.state.ManagerStatus;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import com.gxzn.admin.core.util.DefaultImages;
import com.gxzn.admin.core.util.FileUtils;
import com.gxzn.admin.modular.system.entity.User;
import com.gxzn.admin.modular.system.entity.UserExpand;
import com.gxzn.admin.modular.system.factory.UserFactory;
import com.gxzn.admin.modular.system.model.SupplierDto;
import com.gxzn.admin.modular.system.service.UserExpandService;
import com.gxzn.admin.modular.system.service.UserService;
import com.gxzn.admin.modular.system.wrapper.UserExpandWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(("/supplier"))
public class UserExpandController extends BaseController {

    private static String PREFIX = "/modular/system/supplier/";

    @Autowired
    private UserExpandService userExpandService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProcurementGovProperties procurementGovProperties;

    /**
     * 跳转到查看注册用户列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "supplier.html";
    }

    /**
     * 查看供应商列表
     *
     * @param name
     * @param timeLimit
     * @param examine
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Integer examine) {
        // 拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> page = userExpandService.selectSuppliers(name, beginTime, endTime, examine);
        Page wrapped = new UserExpandWrapper(page).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 冻结注册用户
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结注册用户", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData freeze(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData unfreeze(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 删除注册用户（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除注册用户", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData delete(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.deleteUser(userId);
        return SUCCESS_TIP;
    }

    /**
     * 重置注册用户的密码
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置注册用户密码", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData reset(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        User user = this.userService.getById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到角色分配页面
     */
    @Permission
    @RequestMapping("/role_assign")
    public String roleAssign(@RequestParam Long userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("userId", userId);
        return PREFIX + "supplier_roleassign.html";
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "注册用户分配角色", key = "userId,roleIds", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData setRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userService.assertAuth(userId);
        this.userService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到注册用户审核页面
     */
    @RequestMapping("/to_supplier_audit")
    public String toSupplierAudit(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        SupplierDto supplierDto = new SupplierDto();
        User user = this.userService.getById(userId);
        BeanUtil.copyProperties(supplierDto, user);
        UserExpand byId = this.userExpandService.getById(userId);
        if (byId != null) {
            BeanUtil.copyProperties(supplierDto, byId);
        }
        LogObjectHolder.me().set(supplierDto);
        return PREFIX + "supplier_audit.html";
    }

    /**
     * 获取注册用户详情
     */
    @RequestMapping("/getSupplierInfo")
    @ResponseBody
    public Object getSupplierInfo(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            ShiroUser currentUser = ShiroKit.getUser();
            if (currentUser == null) {
                throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
            }
            userId = currentUser.getId();
            throw new RequestEmptyException();
        }

        this.userService.assertAuth(userId);
        User user = this.userService.getById(userId);
        Map<String, Object> map = UserFactory.removeUnSafeFields(user);

        UserExpand userExpand = this.userExpandService.getById(userId);

        if (userExpand != null) {
            BeanUtil.beanToMap(userExpand, map, false, false);
            String defaultImageUrl = DefaultImages.getSupplierUploadImagePreUrl() + user.getUserId() + "/";
//            map.put("licencePicPath", defaultImageUrl + userExpand.getLicencePic());
//            map.put("licencePicThumbnailPath", defaultImageUrl + getThumbnailsImageName(userExpand.getLicencePic()));
            map.put("licencePicPath", userExpand.getLicencePic());
            map.put("licencePicThumbnailPath", DefaultImages.getThumbnailsImageName(userExpand.getLicencePic()));
            if (ToolUtil.isNotEmpty(userExpand.getAuthorizePic())) {
                List<String> split = StrUtil.split(userExpand.getAuthorizePic(), StrUtil.C_COMMA);
                List<String> imagePaths = CollectionUtil.newArrayList();// 初始化证件原图
                List<String> imageThumbnailPaths = CollectionUtil.newArrayList();// 初始化证件缩率图
                split.forEach(str -> {
//                    imagePaths.add(defaultImageUrl + str);
//                    imageThumbnailPaths.add(defaultImageUrl + getThumbnailsImageName(str));
                    imagePaths.add(str);
                    imageThumbnailPaths.add(DefaultImages.getThumbnailsImageName(str));
                });
                map.put("authorizePicPaths", imagePaths);
                map.put("authorizePicThumbnailPaths", imageThumbnailPaths);
            }
        }
        HashMap<Object, Object> hashMap = CollectionUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(Convert.toStr(user.getDeptId())));

        return ResponseData.success(hashMap);
    }

    /**
     * 注册用户审核
     */
    @RequestMapping("/supplier_audit")
    @BussinessLog(value = "注册用户审核", key = "userId,examine", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData edit(@Valid SupplierDto supplierDto,BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.userExpandService.auditSupplier(supplierDto.getUserId(), supplierDto.getExamine());
        return SUCCESS_TIP;
    }

    /**
     * 跳转到注册用户信息页面
     */
    @RequestMapping("/to_supplier_Info")
    public String toSupplierInfo() {
        Long userId = ShiroKit.getUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        SupplierDto supplierDto = new SupplierDto();
        User user = this.userService.getById(userId);
        BeanUtil.copyProperties(supplierDto, user);
        UserExpand byId = this.userExpandService.getById(userId);
        if (byId != null) {
            BeanUtil.copyProperties(supplierDto, byId);
        }
        LogObjectHolder.me().set(supplierDto);
        return PREFIX + "supplier_audit.html";
    }
    /**
     * 注册用户材料信息修改（新增）
     */
    @RequestMapping("/supplier_update")
    @BussinessLog(value = "注册用户信息完善", key = "address,corporation,corporationPhone,foundTime,credit,bank,bankAccount,validityTime", dict = UserExpendDict.class)
    @Permission
    @ResponseBody
    public ResponseData supplierUpdate(@Valid UserExpand userExpand,
            BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        UserExpand userExpandServiceById = this.userExpandService.getById(userExpand.getUserId());
        if (userExpandServiceById != null) {
            this.userExpandService.updateById(userExpand);
        } else {
            this.userExpandService.save(userExpand);
        }
        return SUCCESS_TIP;
    }

    /**
     * 注册用户单文件上传图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/singleUpload")
    @ResponseBody
    public Object singleUpload(@RequestPart("file") MultipartFile picture, HttpServletRequest request){

        String preUrl = DefaultImages.getSupplierUploadImagePreUrl();
        Long id = ShiroKit.getUser().getId();
        // 文件夹目录
        String fileSavePath = procurementGovProperties.getFileUploadPath() + preUrl + id;

        return ResponseData.success(request.getScheme()+"://"+ request.getServerName()+"/images" + preUrl + id + "/" + FileUtils.singleUpload(picture, fileSavePath,true));
    }

}
