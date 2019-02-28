package com.gxzn.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.gxzn.admin.core.listener.StartupListener;
import com.gxzn.admin.core.listener.StopListener;

import cn.stylefeng.roses.core.config.WebAutoConfiguration;

/**
 * SpringBoot方式启动类
 */
@SpringBootApplication(exclude = {WebAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
public class DiChanAdminApplication {

    private final static Logger logger = LoggerFactory.getLogger(DiChanAdminApplication.class);

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(DiChanAdminApplication.class);
        sa.addListeners(new StartupListener());
        sa.addListeners(new StopListener());
        sa.run(args);
        logger.info("ProcurementGovApplication is success!");
    }
}
