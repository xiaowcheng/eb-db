package com.ebupt.txcy.yellowpagelibbak.aop;


import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class GlobalControllerLog {
    @Pointcut("execution(public * com.ebupt.txcy..*.*Controller.*(..))")
   // @Pointcut("execution(public * com.ebupt.txcy.spamlib.controller.*.*(..))")
    public void controllerLog(){}
    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint){
        log.info("[svc]开始调用该服务");
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 记录下请求内容
        log.debug("[svc]请求URL =====> {}" ,request.getRequestURL().toString());
        log.debug("[svc]请求方式HTTP_METHOD =====> {}" , request.getMethod());
        log.debug("[svc]IP =====>{}" ,request.getRemoteAddr());
        log.debug("[svc]CLASS_METHOD =====>{} " ,joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.debug("[svc]请求参数ARGS =====>{}" , Arrays.toString(joinPoint.getArgs()));

    }
    @AfterReturning(returning = "result",pointcut = "controllerLog()")
    public void doAfterReturning(Object result){
        log.debug("[svc]响应体reponse=====>{}", CommonUtils.objectToJson(result));
        log.info("[svc]服务调用完成");
    }
}
