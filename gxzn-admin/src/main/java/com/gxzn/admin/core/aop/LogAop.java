package com.gxzn.admin.core.aop;

import cn.stylefeng.roses.core.util.HttpContext;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.gxzn.admin.core.common.annotion.BussinessLog;
import com.gxzn.admin.core.common.constant.dictmap.base.AbstractDictMap;
import com.gxzn.admin.core.log.LogManager;
import com.gxzn.admin.core.log.LogObjectHolder;
import com.gxzn.admin.core.log.factory.LogTaskFactory;
import com.gxzn.admin.core.shiro.ShiroKit;
import com.gxzn.admin.core.shiro.ShiroUser;
import com.gxzn.admin.core.util.Contrast;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志记录
 */
@Aspect
@Component
@Slf4j
public class LogAop {

    @Pointcut(value = "@annotation(com.gxzn.admin.core.common.annotion.BussinessLog)")
    public void cutService() {
    }

    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        // 先执行业务
        Object result = point.proceed();

        try {
            handle(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point) throws Exception {

        // 获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();

        // 如果当前用户未登录，不做日志
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return;
        }

        // 获取拦截方法的参数
        String className = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();

        // 获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
        String bussinessName = annotation.value();
        String key = annotation.key();
        Class dictClass = annotation.dict();

        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            sb.append(param);
            sb.append(" & ");
        }

        // 如果涉及到修改,比对变化
        String msg;
        if (bussinessName.contains("修改") || bussinessName.contains("编辑")) {
            Object obj1 = LogObjectHolder.me().get();
            Map<String, String> obj2 = HttpContext.getRequestParameters();
            msg = Contrast.contrastObj(dictClass, key, obj1, obj2);
        } else {
            Map<String, String> parameters = HttpContext.getRequestParameters();
            AbstractDictMap dictMap = (AbstractDictMap) dictClass.newInstance();
            msg = Contrast.parseMutiKey(dictMap, key, parameters);
        }

        LogManager.me()
                .executeLog(LogTaskFactory.bussinessLog(user.getId(), bussinessName, className, methodName, msg));
    }
}