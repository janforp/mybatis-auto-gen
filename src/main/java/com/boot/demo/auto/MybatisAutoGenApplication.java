package com.boot.demo.auto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhucj
 */
@SpringBootApplication
@MapperScan("com.boot.demo.auto.dao")
public class MybatisAutoGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatisAutoGenApplication.class, args);
	}
}
