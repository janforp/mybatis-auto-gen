package com.boot.demo.auto.spring.beanpostprocessor.invokeorder;

import com.boot.demo.auto.util.PrintUtils;

/**
 * BeanA
 *
 * @author zhucj
 * @since 20230824
 */
public class BeanA {

    public BeanA() {
        PrintUtils.println("BeanA构造器被调用");
    }

    public void init() {
        PrintUtils.println("BeanA的init方法被调用");
    }
}