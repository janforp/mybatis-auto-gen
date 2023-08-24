package com.boot.demo.auto.spring.beanpostprocessor.route.impl;

import com.boot.demo.auto.spring.beanpostprocessor.route.VersionEnum;
import com.boot.demo.auto.spring.beanpostprocessor.route.WorkService;
import com.boot.demo.auto.spring.beanpostprocessor.route.anno.Version;
import org.springframework.stereotype.Service;

/**
 * OldWorkService
 *
 * @author zhucj
 * @since 20230824
 */
@Service
@Version(version = VersionEnum.OLD)
public class OldWorkService implements WorkService {

    @Override
    public String work() {
        System.out.println("农业");
        return "农业";
    }

    @Override
    public String sleep() {
        System.out.println("休息啦");
        return "休息啦";
    }
}