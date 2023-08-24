package com.boot.demo.auto.spring.beanpostprocessor.route.processor;

import com.boot.demo.auto.config.CommonConfig;
import com.boot.demo.auto.spring.beanpostprocessor.route.VersionEnum;
import com.boot.demo.auto.spring.beanpostprocessor.route.anno.RouteInjected;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * RouteBeanPostProcessor
 *
 * @author zhucj
 * @since 20230824
 */
@Component
public class RouteBeanPostProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    private final CommonConfig config;

    private final RouteBeanProxyFactory routeBeanProxyFactory = new RouteBeanProxyFactory();

    public RouteBeanPostProcessor(ApplicationContext applicationContext, CommonConfig config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean routeInjected = field.isAnnotationPresent(RouteInjected.class);
            if (!routeInjected) {
                continue;
            }
            boolean anInterface = field.getType().isInterface();
            if (!anInterface) {
                throw new BeanCreationException("RouteInjected 对应的属性一定为接口类型：" + field.getName());
            }

            // 处理该属性属性注入
            try {
                doRouteInject(field, bean, field.getType());
            } catch (IllegalAccessException e) {
                throw new BeanCreationException("RouteInjected 注入发送异常", e);
            }

        }
        return bean;
    }

    private void doRouteInject(Field field, Object bean, Class<?> type) throws IllegalAccessException {
        field.setAccessible(true);

        // 版本数量
        int versionCount = VersionEnum.values().length;
        Map<String, ?> beansMap = applicationContext.getBeansOfType(type);

        int beanCount = beansMap.size();
        if (beanCount > versionCount) {
            throw new IllegalArgumentException("该类型 ：" + type + " 的实现超过了版本数量 " + versionCount);
        }

        if (beanCount == 1) {
            // 只有一个实现，那么直接注入
            Object fieldBean = beansMap.values().iterator().next();
            field.set(bean, fieldBean);
            return;
        }

        // 多个，那么需要路由
        Object proxy = routeBeanProxyFactory.createProxy(config, type, beansMap);
        field.set(bean, proxy);
    }
}
