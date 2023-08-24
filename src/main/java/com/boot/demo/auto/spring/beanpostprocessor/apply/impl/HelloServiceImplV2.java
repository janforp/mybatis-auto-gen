package com.boot.demo.auto.spring.beanpostprocessor.apply.impl;

import com.boot.demo.auto.spring.beanpostprocessor.apply.HelloService;
import org.springframework.stereotype.Service;

/**
 * HelloServiceImplV2
 *
 * @author zhucj
 * @since 20230824
 */
@Service
public class HelloServiceImplV2 implements HelloService {

    @Override
    public void sayHello() {
        System.out.println("Hello From V2");
    }

    @Override
    public void sayHi() {
        System.out.println("Hi From V2");
    }
}
