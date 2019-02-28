package com.gxzn.admin.modular.system.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户传输bean
 */
@Data
public class UserDto {

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
}
