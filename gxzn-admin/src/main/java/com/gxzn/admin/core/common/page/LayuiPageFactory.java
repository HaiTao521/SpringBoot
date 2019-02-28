package com.gxzn.admin.core.common.page;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.stylefeng.roses.core.util.HttpContext;

/**
 * Layui Table默认的分页参数创建
 *
 */
public class LayuiPageFactory {

    /**
     * 获取layui table的分页参数
     */
    public static Page defaultPage() {
        HttpServletRequest request = HttpContext.getRequest();

        //每页多少条数据
        int limit = Integer.valueOf(request.getParameter("limit"));

        //第几页
        int page = Integer.valueOf(request.getParameter("page"));

        return new Page(page, limit);
    }

    /**
     * 创建layui能识别的分页响应参数
     */
    public static LayuiPageInfo createPageInfo(IPage page) {
        LayuiPageInfo result = new LayuiPageInfo();
        result.setCount(page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
