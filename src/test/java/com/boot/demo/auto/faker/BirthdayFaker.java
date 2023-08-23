package com.boot.demo.auto.faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生日生成器
 *
 * @author linglh
 * @since 20200528
 */
public class BirthdayFaker {

    public static String generate() {

        LocalDate startInclusive = LocalDate.now().minusDays(365 * 100);
        LocalDate endExclusive = LocalDate.now();

        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return randomDate.format(fmt);
    }
}
