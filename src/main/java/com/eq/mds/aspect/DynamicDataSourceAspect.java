package com.eq.mds.aspect;

import com.easy.query.core.util.EasyStringUtil;
import com.eq.mds.annotation.DS;
import com.eq.mds.client.EasyMultiEntityQuery;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * create time 2025/11/17 16:03
 * 文件说明
 *
 * @author xuejiaming
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
    @Autowired
    private EasyMultiEntityQuery easyMultiEntityQuery;

    @Around("execution(public * *(..)) && @annotation(com.eq.mds.annotation.DS)")
    public Object interceptorTenantScope(ProceedingJoinPoint pjp) throws Throwable {



        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DS dynamicDataSource = method.getAnnotation(DS.class); //通过反射拿到注解对象
        try {
            //如果需要动态设置可以通过springEL来实现
            if (EasyStringUtil.isNotBlank(dynamicDataSource.value())) {
                easyMultiEntityQuery.setCurrent(dynamicDataSource.value());
            }
            return pjp.proceed();
        } finally {
            easyMultiEntityQuery.clear();
        }
    }
}
