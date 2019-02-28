package com.gxzn.admin.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxzn.admin.core.common.node.TreeviewNode;
import com.gxzn.admin.core.common.node.ZTreeNode;
import com.gxzn.admin.modular.system.entity.Dept;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 */
@Repository
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition, @Param("deptId") String deptId);

    /**
     * 获取所有部门树列表
     */
    List<TreeviewNode> treeviewNodes();
}
