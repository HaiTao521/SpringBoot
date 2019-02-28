package com.gxzn.admin.core.listener.plugin;

import java.util.List;

import org.springframework.context.ApplicationContext;

/**
 * 插件统一管理
 * 
 * @author JohnnyZhang
 */
public class PluginManager {

	private static List<IPlugin> plugins = PluginFactory.init().getPlugins();

	public static PluginManager init() {
		return PluginManagerHolder.instance;
	}

	private PluginManager() {
	}

	private static class PluginManagerHolder {
		private static final PluginManager instance = new PluginManager();
	}

	public void startAll(ApplicationContext context) {
		for (IPlugin plugin : plugins) {
			plugin.start(context);
		}
	}

	public void stopAll(ApplicationContext context) {
		for (IPlugin plugin : plugins) {
			plugin.stop(context);
		}
		plugins.clear();
	}

}
