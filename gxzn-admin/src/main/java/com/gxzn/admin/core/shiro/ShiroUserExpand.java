package com.gxzn.admin.core.shiro;

import java.io.Serializable;

import lombok.Data;

/**
 * ShiroUser扩展类
 * 
 * @author qiangzhang
 *
 */
@Data
public class ShiroUserExpand implements Serializable {

    private static final long serialVersionUID = -4849223656192053572L;

    private Long addressId;

    private String address;

    private String corporation;

    private String corporationPhone;

    private String foundTime;

    private String credit;

    private String bank;

    private String bankAccount;

    private String licencePic;

    private String validityTime;

    private String authorizePic;
}
