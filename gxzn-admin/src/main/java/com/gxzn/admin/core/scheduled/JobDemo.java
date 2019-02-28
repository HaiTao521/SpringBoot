package com.gxzn.admin.core.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class JobDemo {

    @Scheduled(cron = "0 1 0 * * ?")
    public void demo() {

    }
}
