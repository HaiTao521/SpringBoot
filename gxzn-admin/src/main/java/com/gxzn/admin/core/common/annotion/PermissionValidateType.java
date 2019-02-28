package com.gxzn.admin.core.common.annotion;

public enum PermissionValidateType {

    ROLE(1, "验证角色"), URL(2, "验证URL"), RES_CODE(3, "验证资源CODE");

    int code;
    String message;

    PermissionValidateType(int code, String message) {
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
            for (PermissionValidateType s : PermissionValidateType.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
