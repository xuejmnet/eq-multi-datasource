package com.eq.mds.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * create time 2025/11/17 16:04
 * 文件说明
 *
 * @author xuejiaming
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DS {
    /**
     * 动态数据源
     *
     */
    String value() default "";
}
