package com.gxzn.admin.core.common.constant.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxzn.admin.core.common.constant.cache.Cache;
import com.gxzn.admin.core.common.constant.cache.CacheKey;
import com.gxzn.admin.core.common.constant.state.CommodityState;
import com.gxzn.admin.core.common.constant.state.ManagerStatus;
import com.gxzn.admin.core.common.constant.state.MenuStatus;
import com.gxzn.admin.core.common.constant.state.UserExamine;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.modular.system.entity.*;
import com.gxzn.admin.modular.system.mapper.*;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量的生产工厂
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

	private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
	private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
	private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
	private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
	private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
	private NoticeMapper noticeMapper = SpringContextHolder.getBean(NoticeMapper.class);
	// 添加的新mapper

	public static IConstantFactory me() {
		return SpringContextHolder.getBean("constantFactory");
	}

	@Override
	public String getUserNameById(String userId) {
		User user = userMapper.selectById(Convert.toLong(userId));
		if (user != null) {
			return user.getName();
		} else {
			return "--";
		}
	}

	@Override
	public String getUserAccountById(String userId) {
		User user = userMapper.selectById(Convert.toLong(userId));
		if (user != null) {
			return user.getAccount();
		} else {
			return "--";
		}
	}

	@Override
	@Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
	public String getRoleName(String roleIds) {
		if (ToolUtil.isEmpty(roleIds)) {
			return "";
		}
		Long[] roles = Convert.toLongArray(roleIds);
		StringBuilder sb = new StringBuilder();
		for (Long role : roles) {
			Role roleObj = roleMapper.selectById(role);
			if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
				sb.append(roleObj.getName()).append(",");
			}
		}
		return StrUtil.removeSuffix(sb.toString(), ",");
	}

	@Override
	@Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
	public String getSingleRoleName(String roleId) {
		if (ToolUtil.isEmpty(roleId) || "0".equals(roleId)) {
			return "--";
		}
		Role roleObj = roleMapper.selectById(Convert.toStr(roleId));
		if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
			return roleObj.getName();
		}
		return "";
	}

	@Override
	@Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
	public String getSingleRoleTip(String roleId) {
		if (ToolUtil.isEmpty(roleId) || "0".equals(roleId)) {
			return "--";
		}
		Role roleObj = roleMapper.selectById(roleId);
		if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
			return roleObj.getDescription();
		}
		return "";
	}

	@Override
	@Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
	public String getDeptName(String deptId) {
		if (deptId == null) {
			return "";
		} else if (Convert.toLong(deptId, 0L) == 0L) {
			return "顶级";
		} else {
			Dept dept = deptMapper.selectById(Convert.toLong(deptId));
			if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getFullName())) {
				return dept.getFullName();
			}
			return "";
		}
	}

	@Override
	public String getMenuNames(String menuIds) {
		Long[] menus = Convert.toLongArray(menuIds);
		StringBuilder sb = new StringBuilder();
		for (Long menu : menus) {
			Menu menuObj = menuMapper.selectById(menu);
			if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
				sb.append(menuObj.getName()).append(",");
			}
		}
		return StrUtil.removeSuffix(sb.toString(), ",");
	}

	@Override
	public String getMenuName(String menuId) {
		if (ToolUtil.isEmpty(menuId)) {
			return "";
		} else {
			Menu menu = menuMapper.selectById(Convert.toLong(menuId));
			if (menu == null) {
				return "";
			} else {
				return menu.getName();
			}
		}
	}

	@Override
	public Menu getMenuByCode(String code) {
		if (ToolUtil.isEmpty(code)) {
			return new Menu();
		} else if (code.equals("0")) {
			return new Menu();
		} else {
			Menu param = new Menu();
			param.setCode(code);
			QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(param);
			Menu menu = menuMapper.selectOne(queryWrapper);
			if (menu == null) {
				return new Menu();
			} else {
				return menu;
			}
		}
	}

	@Override
	public String getMenuNameByCode(String code) {
		if (ToolUtil.isEmpty(code)) {
			return "";
		} else if (code.equals("0")) {
			return "顶级";
		} else {
			Menu param = new Menu();
			param.setCode(code);
			QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(param);
			Menu menu = menuMapper.selectOne(queryWrapper);
			if (menu == null) {
				return "";
			} else {
				return menu.getName();
			}
		}
	}

	@Override
	public Long getMenuIdByCode(String code) {
		if (ToolUtil.isEmpty(code)) {
			return 0L;
		} else if (code.equals("0")) {
			return 0L;
		} else {
			Menu menu = new Menu();
			menu.setCode(code);
			QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(menu);
			Menu tempMenu = this.menuMapper.selectOne(queryWrapper);
			return tempMenu.getMenuId();
		}
	}

	@Override
	public String getDictName(String dictId) {
		if (ToolUtil.isEmpty(dictId)) {
			return "";
		} else {
			Dict dict = dictMapper.selectById(Convert.toLong(dictId));
			if (dict == null) {
				return "";
			} else {
				return dict.getName();
			}
		}
	}

	@Override
	public String getNoticeTitle(String dictId) {
		if (ToolUtil.isEmpty(dictId)) {
			return "";
		} else {
			Notice notice = noticeMapper.selectById(Convert.toLong(dictId));
			if (notice == null) {
				return "";
			} else {
				return notice.getTitle();
			}
		}
	}

	@Override
	public String getDictsByName(String name, String code) {
		Dict temp = new Dict();
		temp.setName(name);
		QueryWrapper<Dict> queryWrapper = new QueryWrapper<>(temp);
		Dict dict = dictMapper.selectOne(queryWrapper);
		if (dict == null) {
			return "";
		} else {
			QueryWrapper<Dict> wrapper = new QueryWrapper<>();
			wrapper = wrapper.eq("PID", dict.getDictId());
			List<Dict> dicts = dictMapper.selectList(wrapper);
			for (Dict item : dicts) {
				if (item.getCode() != null && item.getCode().equals(code)) {
					return item.getName();
				}
			}
			return "";
		}
	}

	@Override
	public String getSexName(String sexCode) {
		return getDictsByName("性别", sexCode);
	}

	@Override
	public String getStatusName(String status) {
		return ManagerStatus.getDescription(status);
	}

	@Override
	public String getMenuStatusName(String status) {
		return MenuStatus.getDescription(status);
	}

	@Override
	public List<Dict> findInDict(String id) {
		if (ToolUtil.isEmpty(id)) {
			return null;
		} else {
			QueryWrapper<Dict> wrapper = new QueryWrapper<>();
			List<Dict> dicts = dictMapper.selectList(wrapper.eq("PID", Convert.toLong(id)));
			if (dicts == null || dicts.size() == 0) {
				return null;
			} else {
				return dicts;
			}
		}
	}

	@Override
	public String getCacheObject(String para) {
		return LogObjectHolder.me().get().toString();
	}

	@Override
	public List<Long> getSubDeptId(String deptId) {
		QueryWrapper<Dept> wrapper = new QueryWrapper<>();
		wrapper = wrapper.like("PIDS", "%[" + Convert.toLong(deptId) + "]%");
		List<Dept> depts = this.deptMapper.selectList(wrapper);

		ArrayList<Long> deptids = new ArrayList<>();

		if (depts != null && depts.size() > 0) {
			for (Dept dept : depts) {
				deptids.add(dept.getDeptId());
			}
		}

		return deptids;
	}

	@Override
	public List<Long> getParentDeptIds(String deptId) {
		Dept dept = deptMapper.selectById(Convert.toLong(deptId));
		String pids = dept.getPids();
		String[] split = pids.split(",");
		ArrayList<Long> parentDeptIds = new ArrayList<>();
		for (String s : split) {
			parentDeptIds.add(Long.valueOf(StrUtil.removeSuffix(StrUtil.removePrefix(s, "["), "]")));
		}
		return parentDeptIds;
	}

	@Override
	public String getExamineName(Integer examine) {
		return UserExamine.valueOf(examine);
	}


	@Override
	public String getCommodityStateName(Integer state) {
		return CommodityState.valueOf(state);
	}


}
