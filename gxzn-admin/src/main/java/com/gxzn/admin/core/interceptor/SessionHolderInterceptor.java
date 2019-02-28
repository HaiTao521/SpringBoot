package com.gxzn.admin.core.interceptor;

import com.gxzn.admin.core.util.HttpSessionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cn.stylefeng.roses.core.base.controller.BaseController;

/**
 * 静态调用session的拦截器
 */
@Aspect
@Component
public class SessionHolderInterceptor extends BaseController {

    @Pointcut("execution(* com.gxzn.procurement.gov.*..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
        HttpSessionContext.put(super.getHttpServletRequest().getSession());
        try {
            return point.proceed();
        } finally {
            HttpSessionContext.remove();
        }
    }
}
