package com.gxzn.admin.core.listener;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import com.gxzn.admin.core.listener.plugin.IPlugin;
import com.gxzn.admin.core.listener.plugin.IPluginFactory;
import com.gxzn.admin.core.listener.plugin.PluginFactory;
import com.gxzn.admin.core.listener.plugin.PluginManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring启动监听器<br>
 * 可以向其中注入启动监听事件，只需要向实现接口<code>com.gxzn.ares.framework.ystoken.listener.plugin.IPlugin</code>即可
 * 
 * @see IPlugin
 * @author JohnnyZhang
 */
@Slf4j
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>,Ordered {


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			log.debug(">>>>> spring初始化完毕 <<<<<");
			registerPlugins(event.getApplicationContext());
		}
	}

	/**
	 * 插件的启用
	 */
	private void registerPlugins(ApplicationContext context) {
		IPluginFactory plugins = PluginFactory.init();
		// 系统入口初始化
		Map<String, IPlugin> baseInterfaceBeans = context.getBeansOfType(IPlugin.class);
		if (baseInterfaceBeans == null || baseInterfaceBeans.isEmpty()) {
			return;
		}
		log.debug("注册实现IPlugin接口的类");
		for (IPlugin iPlugin : baseInterfaceBeans.values()) {
			plugins.register(iPlugin);
		}
		PluginManager.init().startAll(context);
	}

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
