package com.gxzn.admin.modular.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 供应商编辑传输对象
 * 
 * @author qiangzhang
 */
@Data
public class SupplierDto implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 47035482510708099L;
    private Long userId;
    private String account;
    private String password;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String sex;
    private String email;
    private String phone;
    private String roleId;
    private Long deptId;
    private String status;
    private String avatar;
    private Integer type;
    private Integer examine;
    private Long createUser;

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
