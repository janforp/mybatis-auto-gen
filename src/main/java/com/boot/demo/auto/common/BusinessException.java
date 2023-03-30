package com.boot.demo.auto.common;

/**
 * BusinessException
 *
 * @author zhucj
 * @since 20230323
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message, null);
    }
}