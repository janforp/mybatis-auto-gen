package com.boot.demo.auto.faker;

import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

/**
 * 身份证号码生成器
 *
 * @author linglh
 * @since 20200528
 */
public class AreaCodeFaker {

    /**
     * 身份证号码区位码
     */
    private static final String[] PROVINCE_CODE_ARRAY = {
            "11", "12", "13", "14", "15",
            "21", "22", "23", "31", "31",
            "33", "34", "35", "36", "37",
            "41", "42", "43", "44", "45",
            "46", "50", "51", "52", "53",
            "54", "61", "62", "63", "64",
            "65"
    };

    /**
     * 生成证件号码
     *
     * @return 证件号码
     */
    public static String generate() {
        Random rand = new Random();
        String provinceCode = PROVINCE_CODE_ARRAY[rand.nextInt(PROVINCE_CODE_ARRAY.length)];
        return provinceCode + "0" + RandomUtils.nextInt(0, 4) + "00";
    }
}
