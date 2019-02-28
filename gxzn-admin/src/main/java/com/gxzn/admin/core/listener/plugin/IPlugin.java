package com.gxzn.admin.core.listener.plugin;

import org.springframework.context.ApplicationContext;

/**
 * IPlugin插件接口
 * 
 * @author JohnnyZhang
 */
public interface IPlugin {
	/**
	 * 插件启动
	 */
	void start(ApplicationContext context);

	/**
	 * 插件关闭
	 */
	void stop(ApplicationContext context);
}
