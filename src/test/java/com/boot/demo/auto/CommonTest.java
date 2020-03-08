package com.boot.demo.auto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 类说明：
 *
 * @author zhucj
 * @since 2020-02-17 - 15:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {

    @Test
    public void localDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(format);

        LocalDateTime parse = LocalDateTime.parse(format, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(parse.toString());
    }
}
