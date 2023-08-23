package com.boot.demo.auto.faker;

import java.util.Random;

/**
 * 姓名生成器
 *
 * @author linglh
 * @since 20200528
 */
public class NameFaker {

    /**
     * 姓
     */
    private static final String FAMILY_NAMES = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹";

    /**
     * 名
     */
    private static final String GIVEN_NAMES = "子璇淼国栋夫子瑞堂甜敏尚国贤贺祥晨涛昊轩易轩益辰益帆益冉瑾春瑾昆春齐杨文昊东东雄霖浩晨熙涵溶溶冰枫欣欣宜豪欣慧建政美欣淑慧文轩文杰欣源忠林榕润欣汝慧嘉新建建林亦菲林冰洁佳欣涵涵禹辰淳美泽惠伟洋涵越润丽翔淑华晶莹凌晶苒溪雨涵嘉怡佳毅子辰佳琪紫轩瑞辰昕蕊萌明远欣宜泽远欣怡佳怡佳惠晨茜晨璐运昊汝鑫淑君晶滢润莎榕汕佳钰佳玉晓庆一鸣语晨添池添昊雨泽雅晗雅涵清妍诗悦嘉乐晨涵天赫玥傲佳昊天昊萌萌若萌";

    public static String generate() {
        String familyName = getRandomItem(FAMILY_NAMES);
        String givenName = getRandomItem(GIVEN_NAMES);

        // 一定概率生成三个字的姓名
        if (Math.random() > 0.3) {
            givenName += getRandomItem(GIVEN_NAMES);
        }

        return familyName + givenName;
    }

    private static String getRandomItem(String list) {
        Random rand = new Random();
        int index = rand.nextInt(list.length());
        return String.valueOf(list.charAt(index));
    }
}
