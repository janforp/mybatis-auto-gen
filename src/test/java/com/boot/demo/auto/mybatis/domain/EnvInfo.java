package com.boot.demo.auto.mybatis.domain;

import org.assertj.core.util.Sets;

import java.io.File;
import java.util.Set;

/**
 * EnvInfo
 *
 * @author zhucj
 * @since 20220825
 */
public class EnvInfo {

    public static final String SOURCE_PATH = "src/main/java/";

    public static final String SQLMAP_BASE_PATH = "src/main/resources/mapper/";

    public static final String SCHEMA = "dev_hrtool";

    public static final String FILE_CHARSET = "utf-8";

    public static final String DATA_OBJECT_PACKAGE = "com.boot.demo.auto.dataobject";

    public static final String DAO_PACKAGE = "com.boot.demo.auto.dao";

    public static final String ACCOUNT = "'${@cn.com.servyou.hrbase.dao.util.AccountIdUtils@getAccountId()}'";

    public static final String NEW_LINE = "\n";

    public static final Set<String> USE_DEFAULT_COLUMN_SET = Sets.newLinkedHashSet(
            "is_delete",
            "create_date",
            "modify_date"
    );

    public static final Set<String> ACCOUNT_ID_COLUMN_SET = Sets.newLinkedHashSet(
            "creator_id",
            "modifier_id"
    );

    /**
     * 创建人id字段
     */
    public static final String CREATOR_ID = "creator_id";

    /**
     * 修改人id字段
     */
    public static final String MODIFIER_ID = "modifier_id";

    /**
     * 是否存在逻辑删除
     */
    public static final boolean EXIST_IS_DELETE = true;

    /**
     * 是否存在修改人id
     */
    public static final boolean EXIST_MODIFIER_ID = true;

    public static String buildSourcePath() {
        String basePath = System.getProperty("user.dir");
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separator;
        }
        String sourcePath = basePath + EnvInfo.SOURCE_PATH;
        if (!sourcePath.endsWith(File.separator)) {
            sourcePath = sourcePath + File.separator;
        }
        return sourcePath;
    }

    public static String buildSqlmapBasePath() {
        String basePath = System.getProperty("user.dir");
        String sqlmapBasePath = basePath + File.separator + EnvInfo.SQLMAP_BASE_PATH;
        if (!sqlmapBasePath.endsWith(File.separator)) {
            sqlmapBasePath = sqlmapBasePath + File.separator;
        }
        return sqlmapBasePath;
    }
}
