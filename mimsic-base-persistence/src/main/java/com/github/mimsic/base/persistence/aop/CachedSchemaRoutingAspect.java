package com.github.mimsic.base.persistence.aop;

import com.github.mimsic.base.common.provider.Provider;
import com.github.mimsic.base.persistence.config.SchemaRoutingContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CachedSchemaRoutingAspect {

    @Around("@annotation(CachedSchemaRouting) && args(provider,..)")
    public <K, V> Object around(ProceedingJoinPoint joinPoint, Provider<K, V> provider) throws Throwable {

        SchemaRoutingContextHolder.set(provider.qualifier());
        try {
            return joinPoint.proceed();
        } finally {
            SchemaRoutingContextHolder.clear();
        }
    }
}
