package com.boot.demo.auto.spring.beanpostprocessor.route.anno;

import com.boot.demo.auto.spring.beanpostprocessor.route.VersionEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Version
 *
 * @author zhucj
 * @since 20230824
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Version {

    VersionEnum version() default VersionEnum.OLD;
}
