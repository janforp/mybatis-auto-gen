package com.boot.demo.auto.controller;

import com.boot.demo.auto.spring.beanpostprocessor.route.WorkService;
import com.boot.demo.auto.spring.beanpostprocessor.route.anno.RouteInjected;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WorkController
 *
 * @author zhucj
 * @since 20230824
 */
@RestController
@RequestMapping("/beanpostprocessor/apply/work")
public class WorkController {

    @RouteInjected
    private WorkService workService;

    @GetMapping("/work")
    public void work() {
        workService.work();
    }
}