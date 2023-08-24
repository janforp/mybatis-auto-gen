package com.boot.demo.auto.spring.aware;

import lombok.Getter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * BeanNameAwareBean
 *
 * @author zhucj
 * @since 20230824
 */
@Component
public class BeanNameAwareBean implements BeanNameAware {

    @Getter
    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}