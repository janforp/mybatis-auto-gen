package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
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
        buf.append("package ").append(modalPackage).append(";").append(EnvInfo.NEW_LINE);
        buf.append(EnvInfo.NEW_LINE);

        buf.append("import lombok.Data;").append(EnvInfo.NEW_LINE);

        if (tableInfo.isImportUtil() || tableInfo.isImportSql() || tableInfo.isImportMath()) {
            if (tableInfo.isImportSql()) {
                buf.append("import java.sql.*;").append(EnvInfo.NEW_LINE);
            }
            if (tableInfo.isImportUtil()) {
                buf.append("import java.sql.Date;").append(EnvInfo.NEW_LINE);
            }
            if (tableInfo.isImportMath()) {
                buf.append("import java.math.BigDecimal;").append(EnvInfo.NEW_LINE);
            }
            buf.append(EnvInfo.NEW_LINE);
        }

        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("@Data").append(EnvInfo.NEW_LINE);
        buf.append("public class ").append(modalName).append(" {").append(EnvInfo.NEW_LINE);
        buf.append(EnvInfo.NEW_LINE);

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
            size--;
            boolean lastProperty = size == 0;
            if (lastProperty) {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";");
            } else {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);
            }
        }
        buf.append(EnvInfo.NEW_LINE);
        buf.append("}");
        return buf.toString();
    }

    private static String buildComment(String comment) {
        return "/**\n"
                + "     * " + comment + "\n"
                + "     */" + "\n";
    }
}