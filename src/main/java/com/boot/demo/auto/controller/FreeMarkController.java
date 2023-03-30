package com.boot.demo.auto.controller;

import com.boot.demo.auto.common.ApiResponse;
import com.boot.demo.auto.freemark.pdf.PdfGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * freeMark
 *
 * @author zhucj
 * @since 20230323
 */
@RestController
@RequestMapping("/freeMark")
public class FreeMarkController {

    @GetMapping("/pdf")
    public ApiResponse<Boolean> pdf() {
        PdfGenerator.pdf();
        return ApiResponse.success(true);
    }
}