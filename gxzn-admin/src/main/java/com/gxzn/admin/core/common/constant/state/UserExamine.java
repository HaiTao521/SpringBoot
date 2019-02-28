package com.gxzn.admin.core.common.constant.state;

/**
 * 用户审核状态
 * 
 * @author qiangzhang
 */
public enum UserExamine {
    PASS(0, "通过"), NO_PASS(1, "未通过"),NO_AUTH(2,"未审核"),WAIT_AUTH(3,"待审核");

    int code;
    String message;

    UserExamine(int code, String message) {
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
            for (UserExamine s : UserExamine.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }

}
