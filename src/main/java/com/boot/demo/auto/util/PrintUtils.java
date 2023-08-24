package com.boot.demo.auto.util;

import lombok.experimental.UtilityClass;

/**
 * PrintUtils
 *
 * @author zhucj
 * @since 20230824
 */
@UtilityClass
public class PrintUtils {

    public static void println(Object obj) {
        System.out.println("我要开始打印了哦 **** : " + obj + "， 打印结束了");
    }
}
