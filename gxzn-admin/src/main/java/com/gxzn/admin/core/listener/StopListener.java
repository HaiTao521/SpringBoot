package com.gxzn.admin.core.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.gxzn.admin.core.listener.plugin.PluginManager;

/**
 * 关闭监听器
 * 
 * @author JohnnyZhang
 */
public class StopListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            destroyPlugin(event.getApplicationContext());
        }
    }

    /**
     * 插件的停用
     */
    private void destroyPlugin(ApplicationContext context) {
        PluginManager.init().stopAll(context);
    }

}
