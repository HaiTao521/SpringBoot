package com.gxzn.admin.core.listener.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件工厂
 * 
 * @author JohnnyZhang
 */
public class PluginFactory implements IPluginFactory {
	private static List<IPlugin> plugins = new ArrayList<IPlugin>();

	public static IPluginFactory init() {
		return PluginFactoryHolder.instance;
	}

	private PluginFactory() {
	}

	private static class PluginFactoryHolder {
		private static final IPluginFactory instance = new PluginFactory();
	}

	@Override
	public void register(IPlugin plugin) {
		plugins.add(plugin);
	}

	@Override
	public List<IPlugin> getPlugins() {
		return plugins;
	}

}
