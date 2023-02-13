package com.boot.demo.auto.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * UrlUtils
 *
 * @author zhucj
 * @since 20230223
 */
public class UrlUtils {

    public static String getFromUrl(String url) {

        Map<String, String> map = new HashMap<>();
        consumerFullUrl(url, inputStream -> {
            byte[] bytes = readInputStream(inputStream);
            map.put("a", new String(bytes));
        });
        return map.get("a");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组
     */
    public static byte[] readInputStream(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            int len;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("读取流失败");
        }
    }

    public static void consumerFullUrl(String fullUrl, Consumer<InputStream> inputStreamConsumer) {
        HttpURLConnection conn;
        try {
            URL url = new URL(fullUrl);
            conn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            throw new RuntimeException("读取流失败");
        }
        try (InputStream inputStream = conn.getInputStream()) {
            inputStreamConsumer.accept(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("读取流失败");
        }
    }
}