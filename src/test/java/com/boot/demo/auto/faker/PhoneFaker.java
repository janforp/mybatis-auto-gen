package com.boot.demo.auto.faker;

import java.util.Random;

/**
 * 手机号码生成器
 *
 * @author linglh
 * @since 20200528
 */
public class PhoneFaker {

    private static final String[] PHONE_PREFIX = { "133", "149", "153", "173", "177",
            "180", "181", "189", "199", "130", "131", "132",
            "145", "155", "156", "166", "171", "175", "176", "185", "186", "166", "134", "135",
            "136", "137", "138", "139", "147", "150", "151", "152", "157", "158", "159", "172",
            "178", "182", "183", "184", "187", "188", "198", "170", "171" };

    public static String generate() {
        Random random = new Random();

        //随机出真实号段   使用数组的length属性，获得数组长度，
        //通过Math.random（）*数组长度获得数组下标，从而随机出前三位的号段
        String firstNum = PHONE_PREFIX[random.nextInt(PHONE_PREFIX.length)];
        //随机出剩下的8位数
        StringBuilder lastNum = new StringBuilder();
        final int last = 8;
        for (int i = 0; i < last; i++) {
            //每次循环都从0~9挑选一个随机数
            lastNum.append(random.nextInt(10));
        }
        //最终将号段和尾数连接起来
        return firstNum + lastNum;
    }
}
