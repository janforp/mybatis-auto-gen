package com.boot.demo.auto.faker;

import java.math.BigDecimal;

/**
 * 数据生成器
 *
 * @author linglh
 * @since 20200528
 */
public class Faker {

    /**
     * 随机生成一个机构id
     * 为了区别正常的机构id，该函数返回一个负数
     * 和企业id生成规则一样，内部实现调用了企业id的规则
     *
     * @return 企业id
     */
    public static Long companyId() {
        return CustomerIdFaker.generate();
    }

    /**
     * 随机生成一个企业id
     * 为了区别正常的企业id，该函数返回一个负数
     *
     * @return 企业id
     */
    public static Long customerId() {
        return CustomerIdFaker.generate();
    }

    /**
     * 随机生成一个组织登记序号
     *
     * @return 企业id
     */
    public static String registrationNumber() {
        return RegistrationNumberFaker.generate();
    }

    /**
     * 随机生成一个税号
     *
     * @return 税号
     */
    public static String taxNo() {
        return TaxNoFaker.generate();
    }

    /**
     * 根据区域随机生成一个税号
     *
     * @return 税号
     */
    public static String taxNo(String areaCode) {
        return TaxNoFaker.generate(areaCode);
    }

    /**
     * 生成一个姓名
     *
     * @return 姓名
     */
    public static String name() {
        return NameFaker.generate();
    }

    /**
     * 生成一个身份证号码
     *
     * @return 身份证号码
     */
    public static String licenceNumber() {
        return IdNumberFaker.generate();
    }

    /**
     * 生成一个身份证号码
     *
     * @return 身份证号码
     */
    public static String licenceNumber(String birthday) {
        return IdNumberFaker.generate(birthday);
    }

    /**
     * 生成一个身份证号码
     *
     * @return 身份证号码
     */
    public static String licenceNumber(String birthday, String gender) {
        return IdNumberFaker.generate(birthday, gender);
    }

    /**
     * 生成一个手机号码
     *
     * @return 手机号码
     */
    public static String phone() {
        return PhoneFaker.generate();
    }

    /**
     * 生成uuid
     *
     * @return uuid
     */
    public static String uuid() {
        return UuidFaker.generate();
    }

    /**
     * 生成areaCode
     *
     * @return areaCode
     */
    public static String areaCode() {
        return AreaCodeFaker.generate();
    }

    /**
     * 生成金额字符串（BigDecimal，非负，小数点后两位）
     *
     * @return areaCode
     */
    public static String amountString() {
        return AmountFaker.generateString();
    }

    /**
     * 生成金额（BigDecimal，非负，小数点后两位）
     *
     * @return areaCode
     */
    public static BigDecimal amount() {
        return AmountFaker.generate();
    }

    /**
     * 生成所属期
     * 生成范围（201901～当前所属期）
     *
     * @return 所属期
     */
    public static String period() {
        return PeriodFaker.generate();
    }

    /**
     * 生成Email
     *
     * @return 所属期
     */
    public static String email() {
        return EmailFaker.generate();
    }
}
