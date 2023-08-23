package com.boot.demo.auto.faker;

import lombok.SneakyThrows;

/**
 * 企业id生成器
 *
 * @author linglh
 * @since 20200528
 */
public class CustomerIdFaker {

    @SneakyThrows
    public static Long generate() {
        // 防止连续调用时生成重复数据，所以每次调用都sleep
        Thread.sleep(1);
        return -System.currentTimeMillis();
    }
}
