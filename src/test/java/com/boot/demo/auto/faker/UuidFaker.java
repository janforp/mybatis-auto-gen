package com.boot.demo.auto.faker;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Uuid生成器
 *
 * @author 虎哥
 * @since 20200528
 */
public class UuidFaker {

    /**
     * JDK生成UUID中的连字符"-"
     */
    private static final String HYPHEN = "-";

    /**
     * 生成uuid
     *
     * @return uuid
     */
    public static String generate() {
        return generate(true);
    }

    /**
     * 生成uuid
     *
     * @param excludeHyphen 是否去除"-"
     * @return uuid
     */
    public static String generate(boolean excludeHyphen) {
        String uuid = UUID.randomUUID().toString();
        if (excludeHyphen) {
            uuid = uuid.replace(HYPHEN, StringUtils.EMPTY);
        }

        return uuid;
    }

}
