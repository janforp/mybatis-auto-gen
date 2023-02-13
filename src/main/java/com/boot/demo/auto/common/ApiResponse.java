package com.boot.demo.auto.common;

import lombok.Getter;

/**
 * ApiResponse
 *
 * @author zhucj
 * @since 20230223
 */
@Getter
public class ApiResponse<T> {

    private ApiResponse() {
        // empty
    }

    private int code;

    private String msg;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 0;
        response.msg = null;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(String msg, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 1;
        response.msg = msg;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 1;
        response.msg = msg;
        response.data = null;
        return response;
    }
}