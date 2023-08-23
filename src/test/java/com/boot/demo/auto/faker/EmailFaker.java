package com.boot.demo.auto.faker;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 企业id生成器
 *
 * @author linglh
 * @since 20200528
 */
public class EmailFaker {

    public static String generate() {
        String username = RandomStringUtils.randomAlphanumeric(10);
        String domain = RandomStringUtils.randomAlphanumeric(5) + "." + RandomStringUtils.randomAlphabetic(3);

        return (username + "@" + domain).toLowerCase();
    }

}
