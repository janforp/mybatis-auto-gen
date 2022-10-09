package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 构造数据模型
 *
 * @author zhucj
 * @since 20220825
 */
class DataObjectBuilder {

    static String buildDataObject(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String newLine = "\n";
        buf.append("package ").append(modalPackage).append(";").append(newLine);
        buf.append(newLine);

        buf.append("import lombok.Data;").append(newLine);

        if (tableInfo.isImportUtil() || tableInfo.isImportSql() || tableInfo.isImportMath()) {
            if (tableInfo.isImportSql()) {
                buf.append("import java.sql.*;").append(newLine);
            }
            if (tableInfo.isImportUtil()) {
                buf.append("import java.sql.Date;").append(newLine);
            }
            if (tableInfo.isImportMath()) {
                buf.append("import java.math.BigDecimal;").append(newLine);
            }
            buf.append(newLine);
        }

        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("@Data").append(newLine);
        buf.append("public class ").append(modalName).append(" {").append(newLine);
        buf.append(newLine);

        Map<String, String> columnCommentMap = tableInfo.getColumnCommentMap();

        List<String> columnList = tableInfo.getColumns();
        int size = columnList.size();
        for (String column : columnList) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));

            String comment = columnCommentMap.get(column);
            if (comment != null) {
                String buildComment = buildComment(comment);
                buf.append("    ").append(buildComment);
            }
            System.out.println(size + propertyName);
            size--;
            boolean lastProperty = size == 0;
            if (lastProperty) {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";");
            } else {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";").append(newLine).append(newLine);
            }
        }
        buf.append(newLine);
        buf.append("}");
        return buf.toString();
    }

    private static String buildComment(String comment) {
        return "/**\n"
                + "     * " + comment + "\n"
                + "     */" + "\n";
    }
}