package com.boot.demo.auto.faker;

import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

/**
 * @author linglh
 * @since 2.48.0
 */
public class IdNumberFaker {

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
            "65", "71", "81", "82"
    };

    /**
     * 加权因子
     */
    private static final int[] VERIFY_POWER_NUMBER_ARRAY = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 校验码
     */
    private static final String[] VERIFY_CODE_ARRAY = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

    /**
     * 生成证件号码
     *
     * @return 证件号码
     */
    public static String generate() {
        String birthday = BirthdayFaker.generate();
        return generate(birthday);
    }

    /**
     * 生成证件号码
     *
     * @param birthday 生日
     * @return 证件号码
     */
    public static String generate(String birthday) {

        int gender = RandomUtils.nextInt(0, 2);
        return generate(birthday, String.valueOf(gender));
    }

    /**
     * 生成证件号码
     *
     * @param birthday 生日
     * @param gender 性别
     * @return 证件号码
     */
    public static String generate(String birthday, String gender) {

        String stripBirthday = birthday.replace("-", "");
        String areaCode = getDistrictCode();
        String randomString = String.valueOf(100 + RandomUtils.nextInt(0, 100)).substring(1);
        String licenseNumber = areaCode + stripBirthday + randomString + gender;

        char[] numberArray = licenseNumber.toCharArray();
        int total = 0;
        for (int i = 0; i < numberArray.length; i++) {
            // numberArray[i]-48 => char to int
            total = total + (numberArray[i] - 48) * VERIFY_POWER_NUMBER_ARRAY[i];
        }
        String verifyCode = VERIFY_CODE_ARRAY[total % 11];

        return licenseNumber + verifyCode;
    }

    private static String getDistrictCode() {
        Random rand = new Random();
        String provinceCode = PROVINCE_CODE_ARRAY[rand.nextInt(PROVINCE_CODE_ARRAY.length)];
        return provinceCode + "0" + RandomUtils.nextInt(0, 10) + "00";
    }
}
