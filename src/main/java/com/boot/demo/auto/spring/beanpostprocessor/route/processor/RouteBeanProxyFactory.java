package com.boot.demo.auto.spring.beanpostprocessor.route.processor;

import com.boot.demo.auto.config.CommonConfig;
import com.boot.demo.auto.spring.beanpostprocessor.route.VersionEnum;
import com.boot.demo.auto.spring.beanpostprocessor.route.anno.Version;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import java.util.Map;

/**
 * RouteBeanProxyFactory
 *
 * @author zhucj
 * @since 20230824
 */
public class RouteBeanProxyFactory {

    public Object createProxy(CommonConfig config, Class<?> type, Map<String, ?> beansMap) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(type);
        proxyFactory.addAdvice(new VersionRoutingMethodInterceptor(config, beansMap));
        return proxyFactory.getProxy();
    }

    private static class VersionRoutingMethodInterceptor implements MethodInterceptor {

        private final Map<String, ?> beansMap;

        private final CommonConfig config;

        public VersionRoutingMethodInterceptor(CommonConfig config, Map<String, ?> beansMap) {
            this.beansMap = beansMap;
            this.config = config;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 在这里进行真正的路由调用具体的方法
            Object service = getService();
            return invocation.getMethod().invoke(service, invocation.getArguments());
        }

        private Object getService() {
            String version = config.getVersion();
            VersionEnum versionEnum = VersionEnum.valueOf(version);
            for (Object value : beansMap.values()) {
                VersionEnum versionOfService = getVersion(value);
                if (versionEnum == versionOfService) {
                    return value;
                }
            }
            throw new IllegalStateException("没有该版本 ：" + version + " 的实现");
        }

        private VersionEnum getVersion(Object value) {
            boolean versionPresent = value.getClass().isAnnotationPresent(Version.class);
            if (!versionPresent) {
                return null;
            }
            Version version = value.getClass().getAnnotation(Version.class);
            return version.version();
        }
    }
}