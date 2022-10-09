package com.boot.demo.auto.mybatis.domain;

import java.io.File;

/**
 * EnvInfo
 *
 * @author zhucj
 * @since 20220825
 */
public class EnvInfo {

    public static final String sourcePath = "src/main/java/";

    public static final String sqlmapBasePath = "src/main/resources/mapper/";

    public static final String schema = "dev_hrtool";

    public static final String fileCharset = "utf-8";

    public static final String modalPackage = "com.boot.demo.auto.entity";

    public static final String daoPackage = "com.boot.demo.auto.dao";

    public static final String account = "'${@cn.com.servyou.hrbase.dao.util.AccountIdUtils@getAccountId()}'";

    public static final String newLine = "\n";

    public static String buildSourcePath() {
        String basePath = System.getProperty("user.dir");
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separator;
        }
        String sourcePath = basePath + EnvInfo.sourcePath;
        if (!sourcePath.endsWith(File.separator)) {
            sourcePath = sourcePath + File.separator;
        }
        return sourcePath;
    }

    public static String buildSqlmapBasePath() {
        String basePath = System.getProperty("user.dir");
        String sqlmapBasePath = basePath + File.separator + EnvInfo.sqlmapBasePath;
        if (!sqlmapBasePath.endsWith(File.separator)) {
            sqlmapBasePath = sqlmapBasePath + File.separator;
        }
        return sqlmapBasePath;
    }
}
