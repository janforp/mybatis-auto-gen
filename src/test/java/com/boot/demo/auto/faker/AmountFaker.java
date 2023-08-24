package com.boot.demo.auto.faker;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 * 企业id生成器
 *
 * @author 虎哥
 * @since 20200528
 */
public class AmountFaker {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static BigDecimal generate() {
        return new BigDecimal(generateString());
    }

    public static String generateString() {
        return Math.abs(RANDOM.nextInt(Integer.MAX_VALUE)) + "." + RandomStringUtils.randomNumeric(2);
    }
}
