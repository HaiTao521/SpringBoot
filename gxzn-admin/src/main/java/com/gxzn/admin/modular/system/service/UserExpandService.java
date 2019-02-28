package com.gxzn.admin.modular.system.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gxzn.admin.core.common.constant.Const;
import com.gxzn.admin.core.common.constant.state.UserExamine;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.modular.system.entity.User;
import com.gxzn.admin.modular.system.entity.UserExpand;
import com.gxzn.admin.modular.system.mapper.UserExpandMapper;
import com.gxzn.admin.modular.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSpliter;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 注册用户服务器类
 * 
 * @author qiangzhang
 *
 */
@Service
@Slf4j
public class UserExpandService extends ServiceImpl<UserExpandMapper, UserExpand> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    /**
     * 根据条件查询注册用户列表
     */
    public Page<Map<String, Object>> selectSuppliers(String name, String beginTime, String endTime, Integer examine) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectSuppliers(page, name, beginTime, endTime, examine);
    }

    /**
     * 注册用户审核
     * 
     * @param userId
     * @param examine
     * @return
     */
    //TODO 需要变为Event事件监听模式
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int auditSupplier(Long userId, Integer examine) {
        if (ToolUtil.isOneEmpty(userId, examine)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        User oldUser = userMapper.selectById(userId);
        User user = new User();
        user.setUserId(userId);
        user.setExamine(examine);
        // 若审核通过，改变临时注册用户角色为注册用户角色
        if (UserExamine.PASS.getCode() == examine) {
            List<String> collect = StrSpliter.split(oldUser.getRoleId(), 0).stream()
                    .filter(item -> !item.equals(Const.SUPPLIER_TEMP_ROLE_ID)).collect(Collectors.toList());
            collect.add(Const.SUPPLIER_ROLE_ID);
            user.setRoleId(CollUtil.join(collect, ","));
        }
        int updateById = userMapper.updateById(user);
        if (updateById > 0) {// 发送邮件
            // 建立邮件消息
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // 发送者
            mailMessage.setFrom(mailProperties.getUsername());
            // 接受者
            mailMessage.setTo(oldUser.getEmail());
            // 发送的标题
            mailMessage.setSubject(mailProperties.getProperties().get("subject"));
            // 发送的内容
            mailMessage
                    .setText(UserExamine.PASS.getCode() == examine ? mailProperties.getProperties().get("successtext")
                            : mailProperties.getProperties().get("failtext"));
            // 抄送邮箱
            if (ToolUtil.isNotEmpty(mailProperties.getProperties().get("cc"))) {
                mailMessage.setCc(mailProperties.getProperties().get("cc"));
            }
            try {
                javaMailSender.send(mailMessage);
            } catch (Exception e) {
                log.error("向" + oldUser.getEmail() + "发送邮件失败", e);
            }

        }
        return updateById;
    }

}
