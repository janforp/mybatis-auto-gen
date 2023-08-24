package com.boot.demo.auto.spring.beanpostprocessor.apply;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RoutingSwitch
 *
 * @author zhucj
 * @since 20230824
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RoutingSwitch {

    String value() default "";
}