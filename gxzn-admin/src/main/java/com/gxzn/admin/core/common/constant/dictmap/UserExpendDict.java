package com.gxzn.admin.core.common.constant.dictmap;

import com.gxzn.admin.core.common.constant.dictmap.base.AbstractDictMap;

public class UserExpendDict extends AbstractDictMap {
    @Override
    public void init() {
        put("addressId","注册地址ID");
        put("address","注册地址");
        put("corporation","法人名称");
        put("corporationPhone","联系方式");
        put("foundTime","成立日期");
        put("credit","统一信用代码");
        put("bank","开户银行");
        put("bankAccount","开户行账号");
        put("licencePic","营业执照图片地址");
        put("validityTime","有效期");
        put("authorizePic","授权证明复印件图片地址");
    }

    @Override
    protected void initBeWrapped() {
//        putFieldWrapperMethodName("addressId", "getAddress");
    }
}
