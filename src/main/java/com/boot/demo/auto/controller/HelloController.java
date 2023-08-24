package com.boot.demo.auto.controller;

import com.boot.demo.auto.config.CommonConfig;
import com.boot.demo.auto.spring.beanpostprocessor.apply.impl.HelloServiceImplV1;
import com.boot.demo.auto.spring.beanpostprocessor.apply.impl.HelloServiceImplV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author zhucj
 * @since 20230824
 */
@RestController
@RequestMapping("/beanpostprocessor/apply")
public class HelloController {

    private final CommonConfig config;

    private final HelloServiceImplV1 helloServiceImplV1;

    private final HelloServiceImplV2 helloServiceImplV2;

    public HelloController(CommonConfig config, HelloServiceImplV1 helloServiceImplV1, HelloServiceImplV2 helloServiceImplV2) {
        this.config = config;
        this.helloServiceImplV1 = helloServiceImplV1;
        this.helloServiceImplV2 = helloServiceImplV2;
    }

    @GetMapping("/sayHello")
    public void sayHello() {
        if ("A".equals(config.getVersion())) {
            helloServiceImplV1.sayHello();
        } else {
            helloServiceImplV2.sayHello();
        }
    }

    @GetMapping("/sayHi")
    public void sayHi() {
        if ("A".equals(config.getVersion())) {
            helloServiceImplV1.sayHi();
        } else {
            helloServiceImplV2.sayHi();
        }
    }
}