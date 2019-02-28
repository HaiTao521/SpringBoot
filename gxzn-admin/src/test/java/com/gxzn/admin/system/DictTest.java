package com.gxzn.admin.system;

import com.gxzn.admin.base.BaseJunit;
import com.gxzn.admin.modular.system.mapper.DictMapper;
import com.gxzn.admin.modular.system.service.DictService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 字典服务测试
 *
 * @author fengshuonan
 * @date 2017-04-27 17:05
 */
public class DictTest extends BaseJunit {

    @Resource
    DictService dictService;

    @Resource
    DictMapper dictMapper;

    @Test
    public void deleteTest() {
        this.dictService.delteDict(16L);
    }
}
