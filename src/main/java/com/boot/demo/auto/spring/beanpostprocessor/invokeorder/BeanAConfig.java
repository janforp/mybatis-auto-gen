package com.boot.demo.auto.spring.beanpostprocessor.invokeorder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BeanAConfig
 *
 * @author zhucj
 * @since 20230824
 */
@Configuration
public class BeanAConfig {

    @Bean(initMethod = "init")
    public BeanA getBeanA() {
        return new BeanA();
    }
}