package com.boot.demo.auto.mybatis.domain;

import lombok.Data;

import java.io.File;

/**
 * EnvInfo
 *
 * @author zhucj
 * @since 20220825
 */
@Data
public class EnvInfo {

    private static String basePath = System.getProperty("user.dir");

    private String sourcePath = "src/main/java/";

    private String sqlmapBasePath = "src/main/resources/mapper/";

    private String schema = "dev_hrtool";

    private String fileCharset = "utf-8";

    private String modalPackage = "com.boot.demo.auto.entity";

    private String daoPackage = "com.boot.demo.auto.dao";

    private String modalFilePath;

    private String daoFilePath;

    private String sqlMapperFilePath;

    private String customSqlMapperFilePath;

    public static String buildSourcePath() {
        EnvInfo envInfo = new EnvInfo();
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separator;
        }
        String sourcePath = basePath + envInfo.getSourcePath();
        if (!sourcePath.endsWith(File.separator)) {
            sourcePath = sourcePath + File.separator;
        }
        return sourcePath;
    }

    public static String buildSqlmapBasePath() {
        EnvInfo envInfo = new EnvInfo();

        String sqlmapBasePath = basePath + File.separator + envInfo.getSqlmapBasePath();
        if (!sqlmapBasePath.endsWith(File.separator)) {
            sqlmapBasePath = sqlmapBasePath + File.separator;
        }
        return sqlmapBasePath;
    }
}
