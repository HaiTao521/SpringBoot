package com.gxzn.admin.core.listener.plugin;

import java.util.List;

/**
 * IPluginFactroy插件管理接口
 * 
 * @author JohnnyZhang
 */
public interface IPluginFactory {

	/**
	 * 插件注册
	 * 
	 * @param plugin 插件
	 */
	void register(IPlugin plugin);

	/**
	 * 取得全部插件
	 * 
	 * @return
	 */
	List<IPlugin> getPlugins();
}
