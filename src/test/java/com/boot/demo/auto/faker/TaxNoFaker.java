package com.boot.demo.auto.faker;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

/**
 * 税号生成器
 *
 * @author linglh
 * @since 20200528
 */
public class TaxNoFaker {

    public static final String DEFAULT_AREA_CODE = "4401";

    public static String generate() {
        return generate(DEFAULT_AREA_CODE);
    }

    @SneakyThrows
    public static String generate(String areaCode) {
        // 填充成4位
        areaCode = StringUtils.rightPad(areaCode, 4);

        Thread.sleep(1);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randomString = StringUtils.right(timeStamp, 12);
        // 2 + 4 + 12
        return "91" + areaCode + randomString;
    }

}
