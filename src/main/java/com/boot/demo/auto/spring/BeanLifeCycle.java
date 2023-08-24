package com.boot.demo.auto.spring;

import com.boot.demo.auto.util.PrintUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * aware的英文意思：意识到，察觉到，发觉，发现
 *
 * Aware是一个标记性的超接口（顶级接口），指示了一个Bean有资格通过回调方法的形式获取Spring容器底层组件。实际回调方法被定义在每一个子接口中，而且通常一个子接口只包含一个接口一个参数并且返回值为void的方法。
 *
 * 说白了：只要实现了Aware子接口的Bean都能获取到一个Spring底层组件。
 *
 * @author zhucj
 * @since 20230824
 */
@Component
public class BeanLifeCycle implements
        BeanNameAware,
        BeanClassLoaderAware,
        BeanFactoryAware,
        EnvironmentAware,
        ResourceLoaderAware,
        ApplicationEventPublisherAware,
        ApplicationContextAware,
        InitializingBean,
        BeanPostProcessor, ImportAware, BeanFactoryPostProcessor {

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        PrintUtils.println("2: setBeanClassLoader");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        PrintUtils.println("3: setBeanFactory");
    }

    @Override
    public void setBeanName(String name) {
        PrintUtils.println("1: setBeanName");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        PrintUtils.println("postProcessBeforeInitialization" + " 名称 = " + beanName);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        PrintUtils.println("postProcessAfterInitialization" + " 名称 = " + beanName);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        PrintUtils.println("postProcessBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PrintUtils.println("7: setApplicationContext");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        PrintUtils.println("6: setApplicationEventPublisher");
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        // TODO
        PrintUtils.println("setImportMetadata");
    }

    @Override
    public void setEnvironment(Environment environment) {
        //        String property = environment.getProperty("test.properties.fuck");
        //        PrintUtils.println(property);
        PrintUtils.println("4: setEnvironment");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        // TODO
        PrintUtils.println("5: setResourceLoader");
    }

    @Override
    public void afterPropertiesSet() {
        PrintUtils.println("8: afterPropertiesSet");
    }
}