package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfoConstants;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 构造数据模型
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
class DataObjectBuilder {

    static String buildDataObject(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        buf.append("package ").append(modalPackage).append(";").append(EnvInfoConstants.NEW_LINE);
        buf.append(EnvInfoConstants.NEW_LINE);

        buf.append("import lombok.Data;").append(EnvInfoConstants.NEW_LINE).append(EnvInfoConstants.NEW_LINE);

        if (tableInfo.isImportUtil() || tableInfo.isImportSql() || tableInfo.isImportMath()) {
            if (tableInfo.isImportSql()) {
                buf.append("import java.sql.*;").append(EnvInfoConstants.NEW_LINE);
            }
            if (tableInfo.isImportUtil()) {
                buf.append("import java.sql.Date;").append(EnvInfoConstants.NEW_LINE);
            }
            if (tableInfo.isImportMath()) {
                buf.append("import java.math.BigDecimal;").append(EnvInfoConstants.NEW_LINE);
            }
            buf.append(EnvInfoConstants.NEW_LINE);
        }

        buf.append(MyBatisGenUtils.getAuthorInfo(tableInfo));
        buf.append("@Data").append(EnvInfoConstants.NEW_LINE);
        buf.append("public class ").append(modalName).append(" {").append(EnvInfoConstants.NEW_LINE);
        buf.append(EnvInfoConstants.NEW_LINE);

        Map<String, String> columnCommentMap = tableInfo.getColumnCommentMap();

        List<String> columnList = tableInfo.getColumns();

        List<String> cloneColumnList = new ArrayList<>(columnList);
        cloneColumnList.removeAll(EnvInfoConstants.ACCOUNT_ID_COLUMN_SET);

        int size = cloneColumnList.size();
        for (String column : cloneColumnList) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));

            String comment = columnCommentMap.get(column);
            String buildComment = buildComment(comment, column);
            buf.append("    ").append(buildComment);
            size--;
            boolean lastProperty = size == 0;
            if (lastProperty) {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";");
            } else {
                buf.append("    ").append("private ").append(javaType).append(" ").append(propertyName).append(";").append(EnvInfoConstants.NEW_LINE).append(EnvInfoConstants.NEW_LINE);
            }
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("}");
        return buf.toString();
    }

    private static String buildComment(String comment, String column) {
        return "/**\n"
                + "     * " + ((comment == null || comment.length() == 0) ? column : comment) + "\n"
                + "     */" + "\n";
    }
}