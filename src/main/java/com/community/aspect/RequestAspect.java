package com.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aptx
 */

@Component
@Aspect
public class RequestAspect {

    private final Logger logger = LoggerFactory.getLogger(RequestAspect.class.getName());

    @Pointcut("execution(* com.community.service.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            String remoteHost = request.getRemoteHost();
            String s = joinPoint.getSignature().getDeclaringTypeName() + joinPoint.getSignature().getName();
            logger.info(String.format("用户[%s]访问了[%s]", remoteHost, s));
        }

    }
}
