package com.boot.demo.auto.controller;

import com.boot.demo.auto.common.BusinessException;
import com.boot.demo.auto.freemark.pdf.ExportPdf;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * freeMark
 *
 * @author zhucj
 * @since 20230323
 */
@RestController
@RequestMapping("/freeMark")
public class FreeMarkController {

    @GetMapping(value = "/exportPdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportBillInfo() {
        byte[] bytes;
        HttpHeaders headers;
        ByteArrayOutputStream out = null;
        try {
            ExportPdf exportPdf = new ExportPdf();
            //渲染模板参数
            Map<String, Object> map = new HashMap<>();
            map.put("title", "标题");
            map.put("name", "老王");
            //reportList自己加测试数据
            map.put("reportList", new ArrayList<>());

            out = exportPdf.createPdf(map, "test.ftl", "/templates");
            bytes = out.toByteArray();
            String fileName = "测试.pdf";
            fileName = URLEncoder.encode(fileName, "utf-8");
            headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
        } catch (Exception e) {
            throw new BusinessException("导出pdf失败!");
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}