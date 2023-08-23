package com.boot.demo.auto.faker;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 企业id生成器
 *
 * @author linglh
 * @since 20200528
 */
public class RegistrationNumberFaker {

    public static final String NUMBER_PREFIX = "1011744";

    @SneakyThrows
    public static String generate() {
        return NUMBER_PREFIX + RandomStringUtils.randomNumeric(13);
    }
}
