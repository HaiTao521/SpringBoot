package com.gxzn.admin.core.model;

import java.util.HashMap;
import java.util.Map;

public class AjaxResult extends HashMap<String, Object> {

    /**
     * 
     */
    private static final long serialVersionUID = -4697670673485290038L;

    public AjaxResult() {
        put("success", true);
        put("desc", "success");
    }

    public static AjaxResult error() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("success", false);
        return ajaxResult;
    }

    public static AjaxResult error(String desc) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("success", false);
        ajaxResult.put("desc", desc);
        return ajaxResult;
    }

    public static AjaxResult ok(Object result, String desc) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("result", result);
        ajaxResult.put("desc", desc);
        return ajaxResult;
    }
    
    public static AjaxResult ok(Object result) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("result", result);
        return ajaxResult;
    }

    public static AjaxResult ok(Map<String, Object> map) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.putAll(map);
        return ajaxResult;
    }

    public static AjaxResult ok() {
        return new AjaxResult();
    }

    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
