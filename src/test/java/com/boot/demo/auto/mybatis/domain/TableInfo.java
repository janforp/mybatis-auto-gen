package com.boot.demo.auto.mybatis.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * TableInfo
 *
 * @author zhucj
 * @since 20220825
 */
@Data
public class TableInfo {

    private String tableName;

    private List<String> primaryKeys;

    private boolean primaryKeyAutoIncrement;

    private List<String> columns;

    private Map<String, String> columnTypes;

    private Map<String, String> columnCommentMap;

    private Map<String, Integer> columnSizes;

    // 是否需要导入包java.util.*
    private boolean importUtil = false;

    // 是否需要导入包java.sql.*
    private boolean importSql = false;

    // 是否需要导入包java.math.*
    private boolean importMath = false;
}
