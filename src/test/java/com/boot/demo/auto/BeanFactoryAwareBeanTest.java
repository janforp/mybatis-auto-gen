package com.boot.demo.auto;

import com.boot.demo.auto.spring.aware.BeanFactoryAwareBean;
import com.boot.demo.auto.spring.aware.BeanNameAwareBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BeanFactoryAwareBeanTest
 *
 * @author zhucj
 * @since 20230824
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BeanFactoryAwareBeanTest {

    @Autowired
    private BeanFactoryAwareBean factoryAwareBean;

    @Autowired
    private BeanNameAwareBean nameAwareBean;

    @Test
    public void test() {
        Object bean = factoryAwareBean.getBeanFactory().getBean(nameAwareBean.getBeanName());
        Assert.assertEquals(bean, nameAwareBean);
    }
}