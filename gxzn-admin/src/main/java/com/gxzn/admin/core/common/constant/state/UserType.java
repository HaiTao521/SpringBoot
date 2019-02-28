package com.gxzn.admin.core.common.constant.state;

/**
 * 用户类型
 * 
 * @author qiangzhang
 */
public enum UserType {
    PLATEFORM(0, "平台用户"), SUPPLIER(1, "供应商用户");

    int code;
    String message;

    UserType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (UserType s : UserType.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }

}
