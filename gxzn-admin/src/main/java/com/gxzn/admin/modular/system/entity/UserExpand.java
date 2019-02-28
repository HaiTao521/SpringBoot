package com.gxzn.admin.modular.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户拓展信息表
 * @author qiangzhang
 *
 */
@TableName("sys_user_expand")
@Data
public class UserExpand implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -8265944170352542029L;
    
    @TableId(value="user_id",type=IdType.INPUT)
    private Long userId;
    
    @TableField("address_id")
    private Long addressId;
    
    @TableField("address")
    private String address;
    
    @TableField("corporation")
    private String corporation;
    
    @TableField("corporation_phone")
    private String corporationPhone;
    
    @TableField("found_time")
    private String foundTime;
    
    @TableField("credit")
    private String credit;
    
    @TableField("bank")
    private String bank;
    
    @TableField("bank_account")
    private String bankAccount;
    
    @TableField("licence_pic")
    private String licencePic;
    
    @TableField("validity_time")
    private String validityTime;
    
    @TableField("authorize_pic")
    private String authorizePic;
}
