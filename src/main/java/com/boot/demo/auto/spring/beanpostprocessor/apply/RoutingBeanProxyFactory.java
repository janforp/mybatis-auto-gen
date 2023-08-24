package com.boot.demo.auto.spring.beanpostprocessor.apply;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * RoutingBeanProxyFactory
 *
 * @author zhucj
 * @since 20230824
 */
public class RoutingBeanProxyFactory {

    private RoutingBeanProxyFactory() {
        // empty
    }

    public static Object createProxy(Class<?> type, Map<String, ?> candidates) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(type);
        proxyFactory.addAdvice(new VersionRoutingMethodInterceptor(type, candidates));
        return proxyFactory.getProxy();
    }

    static class VersionRoutingMethodInterceptor implements MethodInterceptor {

        private String classSwitch;

        private final Object beanOfSwitchOn;

        private final Object beanOfSwitchOff;

        public VersionRoutingMethodInterceptor(Class<?> targetClass, Map<String, ?> beans) {
            String interfaceName = StringUtils.uncapitalize(targetClass.getSimpleName());
            if (targetClass.isAnnotationPresent(RoutingSwitch.class)) {
                this.classSwitch = targetClass.getAnnotation(RoutingSwitch.class).value();
            }
            String onBean = buildBeanName(interfaceName, true);
            this.beanOfSwitchOn = beans.get(onBean);

            String offBean = buildBeanName(interfaceName, false);
            this.beanOfSwitchOff = beans.get(offBean);
        }

        private String buildBeanName(String interfaceName, boolean isSwitchOn) {
            return interfaceName + "Impl" + (isSwitchOn ? "V2" : "V1");
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            String switchName = this.classSwitch;
            if (method.isAnnotationPresent(RoutingSwitch.class)) {
                switchName = method.getAnnotation(RoutingSwitch.class).value();
            }
            if (StringUtils.isBlank(switchName)) {
                throw new IllegalStateException("RoutingSwitch 的 value 为空，method：" + method.getName());
            }
            return invocation.getMethod().invoke(getTargetBean(switchName), invocation.getArguments());
        }

        private Object getTargetBean(String switchName) {
            boolean switchOn = RoutingVersion.B.equals(switchName);
            return switchOn ? beanOfSwitchOn : beanOfSwitchOff;
        }
    }
}