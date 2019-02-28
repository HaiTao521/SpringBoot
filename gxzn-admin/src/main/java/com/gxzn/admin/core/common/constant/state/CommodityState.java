package com.gxzn.admin.core.common.constant.state;

/**
 * 商品上下架状态
 */
public enum CommodityState {

    STATE(0, "未上架"), NO_STATE(1, "已上架");

    int state;
    String message;

    CommodityState(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer state) {
        if (state == null) {
            return "";
        } else {
            for (CommodityState s : CommodityState.values()) {
                if (s.getState() == state) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
