package com.gxzn.admin.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.modular.system.entity.Notice;
import com.gxzn.admin.modular.system.mapper.NoticeMapper;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }
}
