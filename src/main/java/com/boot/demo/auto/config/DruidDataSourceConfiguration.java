package com.boot.demo.auto.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author janita
 * @since 2018/9/9 - 上午10:51
 * 类说明：数据源bean
 */
@Configuration
public class DruidDataSourceConfiguration {

	@Value("${spring.datasource.url}")
    private String dbUrl;  
      
    @Value("${spring.datasource.username}")
    private String username;  
      
    @Value("${spring.datasource.password}")
    private String password;  
      
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;  
      
 

    @Bean
    @Primary
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setPassword(password);
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setDriverClassName(driverClassName);
        return datasource;
    }
}
