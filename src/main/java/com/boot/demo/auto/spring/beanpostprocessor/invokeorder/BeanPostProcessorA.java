package com.boot.demo.auto.spring.beanpostprocessor.invokeorder;

import com.boot.demo.auto.util.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 我要开始打印了哦 **** : BeanA构造器被调用， 打印结束了
 * 我要开始打印了哦 **** : BeanA postProcessBeforeInitialization， 打印结束了
 * 我要开始打印了哦 **** : BeanA的init方法被调用， 打印结束了
 * 我要开始打印了哦 **** : BeanA postProcessAfterInitialization， 打印结束了
 *
 * @author zhucj
 * @since 20230824
 */
@Component
public class BeanPostProcessorA implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass() == BeanA.class) {
            PrintUtils.println("BeanA postProcessAfterInitialization");
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass() == BeanA.class) {
            PrintUtils.println("BeanA postProcessBeforeInitialization");
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}