package com.gxzn.admin.modular.system.service;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxzn.admin.core.common.constant.Const;
import com.gxzn.admin.core.common.constant.factory.ConstantFactory;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import com.gxzn.admin.core.common.node.TreeviewNode;
import com.gxzn.admin.core.common.node.ZTreeNode;
import com.gxzn.admin.core.common.page.LayuiPageFactory;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.modular.system.entity.Dept;
import com.gxzn.admin.modular.system.mapper.DeptMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 */
@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept> {

    @Resource
    private DeptMapper deptMapper;

    /**
     * 新增部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDept(Dept dept) {

        if (ToolUtil.isOneEmpty(dept, dept.getSimpleName(), dept.getFullName(), dept.getPid())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        // 完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.save(dept);
    }

    /**
     * 修改部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void editDept(Dept dept) {

        if (ToolUtil.isOneEmpty(dept, dept.getDeptId(), dept.getSimpleName(), dept.getFullName(), dept.getPid())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        // 完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.updateById(dept);
    }

    /**
     * 删除部门
     */
    @Transactional
    public void deleteDept(Long deptId) {
        if (ToolUtil.isEmpty(deptId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        // 不能删除供应商部门
        if (deptId.equals(Const.SUPPLIER_DEPT_ID)) {
            throw new ServiceException(BizExceptionEnum.DEPT_DELETE_SUPPLIER);
        }
        Dept dept = deptMapper.selectById(deptId);

        // 缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(Convert.toStr(deptId)));

        // 根据like查询删除所有级联的部门
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper = wrapper.like("PIDS", "%[" + dept.getDeptId() + "]%");
        List<Dept> subDepts = deptMapper.selectList(wrapper);
        for (Dept temp : subDepts) {
            this.removeById(temp.getDeptId());
        }

        this.removeById(dept.getDeptId());
    }

    /**
     * 获取ztree的节点列表
     */
    public List<ZTreeNode> tree() {
        return this.baseMapper.tree();
    }

    /**
     * 获取ztree的节点列表
     */
    public List<TreeviewNode> treeviewNodes() {
        return this.baseMapper.treeviewNodes();
    }

    /**
     * 获取所有部门列表
     */
    public Page<Map<String, Object>> list(String condition, String deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition, deptId);
    }

    /**
     * 设置部门的父级ids
     */
    private void deptSetPids(Dept dept) {
        if (ToolUtil.isEmpty(dept.getPid()) || dept.getPid().equals(0L)) {
            dept.setPid(0L);
            dept.setPids("[0],");
        } else {
            Long pid = dept.getPid();
            Dept temp = this.getById(pid);
            String pids = temp.getPids();
            dept.setPid(pid);
            dept.setPids(pids + "[" + pid + "],");
        }
    }
}
