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
@Version(version = VersionEnum.NEW)
public class NewWorkService implements WorkService {

    @Override
    public void work() {
        System.out.println("工业");
    }
}