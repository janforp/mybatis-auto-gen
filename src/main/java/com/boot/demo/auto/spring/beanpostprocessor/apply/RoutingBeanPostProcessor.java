package com.boot.demo.auto.spring.beanpostprocessor.apply;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * RoutingBeanPostProcessor
 *
 * @author zhucj
 * @since 20230824
 */
@Component
public class RoutingBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RoutingInjected.class)) {
                if (!field.getType().isInterface()) {
                    throw new BeanCreationException("RoutingInjected field must be 接口: " + field.getName());
                }
                try {
                    this.handleRoutingInjected(field, bean, field.getType());
                } catch (IllegalAccessException e) {
                    throw new BeanCreationException("Exception thrown when handleAutowiredRouting", e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void handleRoutingInjected(Field field, Object bean, Class<?> type) throws IllegalAccessException {
        Map<String, ?> candidates = applicationContext.getBeansOfType(type);
        field.setAccessible(true);
        if (candidates.size() == 1) {
            field.set(bean, candidates.values().iterator().next());
            return;
        }
        if (candidates.size() == 2) {
            Object proxy = RoutingBeanProxyFactory.createProxy(type, candidates);
            field.set(bean, proxy);
            return;
        }
        throw new IllegalArgumentException("查询到了多余2个的类型 ：" + type);
    }
}