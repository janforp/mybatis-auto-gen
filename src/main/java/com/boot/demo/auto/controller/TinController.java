package com.boot.demo.auto.controller;

import com.boot.demo.auto.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TinController
 *
 * @author zhucj
 * @since 20230223
 */
@RestController
@RequestMapping("/tin")
public class TinController {

    @GetMapping("/get")
    public ApiResponse<Integer> get() {
        return ApiResponse.success(1);
    }
}
