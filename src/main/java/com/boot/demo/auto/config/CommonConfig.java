package com.boot.demo.auto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WeChatConfig
 *
 * @author zhucj
 * @since 20220526
 */
@ConfigurationProperties(prefix = "commonconfig")
@Component
@Data
public class CommonConfig {

    /**
     * 是否校验
     */
    private String version;
}