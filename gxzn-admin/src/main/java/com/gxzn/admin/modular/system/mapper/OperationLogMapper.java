package com.gxzn.admin.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.modular.system.entity.OperationLog;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作日志 Mapper 接口
 * </p>
 */
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 获取操作日志
     */
    List<Map<String, Object>> getOperationLogs(@Param("page") Page page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName, @Param("logType") String logType);


}
