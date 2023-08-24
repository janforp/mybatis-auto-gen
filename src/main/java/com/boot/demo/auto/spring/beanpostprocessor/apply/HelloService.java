package com.boot.demo.auto.spring.beanpostprocessor.apply;

/**
 * IHelloService
 *
 * @author zhucj
 * @since 20230824
 */
@RoutingSwitch("hello.switch")
public interface HelloService {

    @RoutingSwitch("A")
    void sayHello();

    void sayHi();
}