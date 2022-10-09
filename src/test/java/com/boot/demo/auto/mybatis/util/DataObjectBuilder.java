package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.Map;

/**
 * 构造数据模型
 *
 * @author zhucj
 * @since 20220825
 */
@SuppressWarnings("all")
class DataObjectBuilder {

    static String buildModal(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String newLine = "\n";
        buf.append("package ").append(modalPackage).append(";").append(newLine);
        buf.append(newLine);

        if (tableInfo.isImportUtil() || tableInfo.isImportSql() || tableInfo.isImportMath()) {
            if (tableInfo.isImportSql()) {
                buf.append("import java.sql.*;").append(newLine);
            }
            if (tableInfo.isImportUtil()) {
                buf.append("import java.util.*;").append(newLine);
            }
            if (tableInfo.isImportMath()) {
                buf.append("import java.math.*;").append(newLine);
            }
            buf.append(newLine);
        }

        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("public class ").append(modalName).append(" {").append(newLine);
        buf.append(newLine);

        Map<String, String> columnCommentMap = tableInfo.getColumnCommentMap();
        int i = 0;
        int fullConstructorAddFieldIndex = 0;
        for (String column : tableInfo.getColumns()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String propertyNameInitCap = MyBatisGenUtils.initCap(propertyName);
            String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));

            String comment = columnCommentMap.get(column);
            if (comment != null) {
                buf.append("    ").append("    /** ").append(newLine).append("         * ").append(comment).append(newLine).append("    */").append(newLine);
            }
            buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";").append(newLine);

            if (i == 0) {

            }
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 这个字段是主键,且是自增长,就不添加到full constructor
            } else {
            }

            i++;
        }
        buf.append(newLine);
        buf.append("}");
        return buf.toString();
    }
}
