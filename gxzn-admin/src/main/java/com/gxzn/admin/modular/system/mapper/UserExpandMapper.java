package com.gxzn.admin.modular.system.mapper;

import java.util.Map;

import com.gxzn.admin.modular.system.entity.UserExpand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户拓展信息表 Mapper 接口
 * </p>
 */
@Repository
public interface UserExpandMapper extends BaseMapper<UserExpand> {
    /**
     * 根据条件查询注册用户列表
     */
    Page<Map<String, Object>> selectSuppliers(@Param("page") Page page, @Param("name") String name,
            @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("examine") Integer examine);
}
