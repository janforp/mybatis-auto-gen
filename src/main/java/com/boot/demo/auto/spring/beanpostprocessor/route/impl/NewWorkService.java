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
    public String work() {
        System.out.println("工业");
        return "工业";
    }

    @Override
    public String sleep() {
        System.out.println("工业不需要休息，继续加班");
        return "工业不需要休息，继续加班";
    }
}