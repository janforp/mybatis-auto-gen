package com.boot.demo.auto.faker;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 所属期生成器
 *
 * @author linglh
 * @since 20200723
 */
public class PeriodFaker {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final DateTimeFormatter PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static String generate() {
        YearMonth startYearMonth = YearMonth.of(2019, 1);

        LocalDate prevMonth = LocalDate.now().minusMonths(1);
        YearMonth endYearMonth = YearMonth.of(prevMonth.getYear(), prevMonth.getMonth());

        int months = (int) ChronoUnit.MONTHS.between(startYearMonth, endYearMonth);
        YearMonth result = startYearMonth.plusMonths(RANDOM.nextInt(months));

        return result.format(PERIOD_FORMATTER);
    }
}
