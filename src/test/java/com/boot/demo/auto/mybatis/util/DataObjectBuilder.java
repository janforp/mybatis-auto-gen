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
public class DataObjectBuilder {

    public static String buildModal(TableInfo tableInfo, String modalPackage) {
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
        buf.append("public class ").append(modalName).append(" implements java.io.Serializable {").append(newLine);
        buf.append(newLine);
        buf.append("    // Fields").append(newLine);
        buf.append(newLine);
        StringBuilder methods = new StringBuilder(2048);
        StringBuilder constructors = new StringBuilder(2048);
        StringBuilder constructorInners = new StringBuilder(1024);
        constructors.append("    ").append("// Constructors").append(newLine);
        {
            // default constructor
            constructors.append(newLine);
            constructors.append("    ").append("/**").append(newLine);
            constructors.append("    ").append(" * default constructor").append(newLine);
            constructors.append("    ").append(" */").append(newLine);
            constructors.append("    ").append("public ").append(modalName).append("() {").append(newLine);
            constructors.append("    ").append("}").append(newLine);
        }
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
                // 第一个属性
                constructors.append(newLine);
                constructors.append("    ").append("/**").append(newLine);
                constructors.append("    ").append(" * full constructor").append(newLine);
                constructors.append("    ").append(" */").append(newLine);
                constructors.append("    ").append("public ").append(modalName).append("(");
                methods.append("    ").append("// Property accessors").append(newLine);
            }
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 这个字段是主键,且是自增长,就不添加到full constructor
            } else {
                constructorInners.append("        ").append("this.").append(propertyName).append(" = ").append(propertyName).append(";").append(newLine);
                if (fullConstructorAddFieldIndex == 0) {
                    constructors.append(javaType).append(" ").append(propertyName);
                }
                if (fullConstructorAddFieldIndex != 0) {
                    constructors.append(", ").append(javaType).append(" ").append(propertyName);
                }
                fullConstructorAddFieldIndex++;
            }
            {
                methods.append(newLine);
                if (comment != null) {
                    methods.append("    ").append("/**").append(newLine);
                    methods.append("    ").append(" * ").append(comment).append(newLine);
                    methods.append("    ").append(" */").append(newLine);
                }
                methods.append("    ").append("public ").append(javaType).append(" get")
                        .append(propertyNameInitCap)
                        .append("() {").append(newLine);

                methods.append("        ").append("return this.").append(propertyName).append(";")
                        .append(newLine);

                methods.append("    ").append("}").append(newLine);
                methods.append(newLine);
                if (comment != null) {
                    methods.append("    ").append("/**").append(newLine);
                    methods.append("    ").append(" * ").append(comment).append(newLine);
                    methods.append("    ").append(" */").append(newLine);
                }
                methods.append("    ").append("public void set")
                        .append(propertyNameInitCap)
                        .append("(").append(javaType).append(" ").append(propertyName).append(
                        ") {").append(newLine);
                methods.append("        ").append("this.").append(propertyName).append(" = ")
                        .append(propertyName).append(";")
                        .append(newLine);
                methods.append("    ").append("}").append(newLine);
            }
            if (i == tableInfo.getColumns().size() - 1) {
                // 最后一个属性
                constructors.append(") {").append(newLine);
                constructors.append(constructorInners);
                constructors.append("    ").append("}").append(newLine);
            }
            i++;
        }
        buf.append(newLine);
        buf.append(constructors);
        buf.append(newLine);
        buf.append(methods);
        buf.append(newLine);
        buf.append("}");
        return buf.toString();
    }
}
