package com.github.mimsic.base.persistence.aop;

import com.github.mimsic.base.persistence.config.SchemaRoutingContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SimpleSchemaRoutingAspect {

    @Around("@annotation(SimpleSchemaRouting) && args(schema,..)")
    public Object around(ProceedingJoinPoint joinPoint, String schema) throws Throwable {

        SchemaRoutingContextHolder.set(schema);
        try {
            return joinPoint.proceed();
        } finally {
            SchemaRoutingContextHolder.clear();
        }
    }
}
