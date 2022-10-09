package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * SqlMapperBuilder
 *
 * @author zhucj
 * @since 20220825
 */
class SqlMapperBuilder {

    public static String buildSqlMapper(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        EnvInfo info = new EnvInfo();
        String daoName = info.getDaoPackage() + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
        String newLine = "\n";
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(newLine);
        buf.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append(newLine);
        buf.append("<mapper namespace=\"").append(daoName).append("\">").append(newLine);
        List<String> columns = tableInfo.getColumns();
        int columnsSize = columns.size();
        List<String> columnsIds = new ArrayList<>(columnsSize);
        List<String> columnsNotIds = new ArrayList<>(columnsSize);
        for (String column : columns) {
            if (tableInfo.getPrimaryKeys().contains(column)) {
                columnsIds.add(column);
            } else {
                columnsNotIds.add(column);
            }
        }
        {
            List<String> newColumnList = new ArrayList<>(columnsSize);
            newColumnList.addAll(columnsIds);
            newColumnList.addAll(columnsNotIds);
            columns = newColumnList;
        }
        {
            // BaseResultMap
            buf.append("    ").append("<resultMap id=\"BaseResultMap\" type=\"").append(modalName).append("\">").append(newLine);
            for (String column : columns) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                buf.append("        ");
                if (tableInfo.getPrimaryKeys().contains(column)) {
                    buf.append("<id");
                } else {
                    buf.append("<result");
                }
                buf.append(" column=\"").append(column).append("\" property=\"").append(propertyName).append("\" jdbcType=\"").append(jdbcType).append("\"/>").append(newLine);
            }
            buf.append("    ").append("</resultMap>").append(newLine);
        }
        {
            // SELECT_All_Column
            buf.append("    ").append("<sql id=\"SELECT_All_Column\">").append(newLine);
            buf.append("        ");
            buf.append("SELECT ");
            int i = 0;
            for (String column : columns) {
                if (i > 0) {
                    buf.append(", ");
                    if (i % 5 == 0) {
                        buf.append(newLine).append("        ");
                    }
                }
                buf.append(caseDbSensitiveWords(column));
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</sql>").append(newLine);
        }
        {
            // selectByPrimaryKey
            buf.append("    ").append("<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("SELECT *").append(newLine);
            buf.append("        ").append("FROM ").append(tableName).append(newLine);
            buf.append("        ").append("WHERE ");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</select>").append(newLine);
        }
        {
            // deleteByPrimaryKey
            buf.append("    ").append("<delete id=\"deleteByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("DELETE FROM ").append(tableName).append(newLine);
            buf.append("        ").append("WHERE ");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</delete>").append(newLine);
        }
        String selectKeyBuf = null;
        {
            // insert
            buf.append("    ").append("<insert id=\"insert\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ");
            int i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                if (i > 0) {
                    buf.append(", ");
                    if ((i + 2) % 3 == 0) {
                        buf.append(newLine).append("          ");
                    }
                }
                buf.append(caseColumn);
                i++;
            }
            buf.append(" )").append(newLine);
            buf.append("        ").append("VALUES ( ");
            i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                    if ((i + 2) % 3 == 0) {
                        buf.append(newLine).append("          ");
                    }
                }
                buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(" )").append(newLine);
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().size() == 1) {
                StringBuilder selectKeyBufTmp = new StringBuilder();
                // 主键是否是自增长(且只有一个主键)
                String column = tableInfo.getPrimaryKeys().get(0);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if ("Integer".equals(javaType)) {
                    javaType = "int";
                } else {
                    javaType = javaType.toLowerCase();
                }
                selectKeyBufTmp.append("        ").append("<selectKey keyProperty=\"").append(propertyName).append("\" resultType=\"").append(javaType).append("\">").append(newLine);
                selectKeyBufTmp.append("            ").append("SELECT LAST_INSERT_ID() AS ").append(propertyName).append(newLine);
                selectKeyBufTmp.append("        ").append("</selectKey>").append(newLine);
                selectKeyBuf = selectKeyBufTmp.toString();
                buf.append(selectKeyBuf);
            }
            buf.append("    ").append("</insert>").append(newLine);
        }
        {
            // insertSelective
            buf.append("    ").append("<insert id=\"insertSelective\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(newLine);
            buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(newLine);
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                boolean isPk = tableInfo.getPrimaryKeys().contains(column);
                if (!isPk) {
                    buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(newLine);
                }
                buf.append("                ").append(caseColumn);
                buf.append(",");
                buf.append(newLine);
                if (!isPk) {
                    buf.append("            ").append("</if>").append(newLine);
                }
            }
            buf.append("        ").append("</trim>").append(newLine);
            buf.append("        ").append("VALUES").append(newLine);
            buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(newLine);
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                boolean isPk = tableInfo.getPrimaryKeys().contains(column);
                if (!isPk) {
                    buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(newLine);
                }
                buf.append("                ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                buf.append(",");
                buf.append(newLine);
                if (!isPk) {
                    buf.append("            ").append("</if>").append(newLine);
                }
            }
            buf.append("        ").append("</trim>").append(newLine);
            if (selectKeyBuf != null && selectKeyBuf.length() > 0) {
                buf.append(selectKeyBuf);
            }
            buf.append("    ").append("</insert>").append(newLine);
        }
        {
            // insertBatch
            buf.append("    ").append("<insert id=\"insertBatch\" parameterType=\"java.util.List\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ");
            int i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                if (i > 0) {
                    buf.append(", ");
                    if ((i + 2) % 3 == 0) {
                        buf.append(newLine).append("          ");
                    }
                }
                buf.append(caseColumn);
                i++;
            }
            buf.append(" )").append(newLine);
            buf.append("        ").append("VALUES").append(newLine);
            buf.append("        ").append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" >").append(newLine);
            buf.append("            ").append("(");
            i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                    if ((i + 2) % 3 == 0) {
                        buf.append(newLine).append("            ");
                    }
                } else {
                    buf.append(newLine).append("            ");
                }
                buf.append("#{item.").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("            ").append(")").append(newLine);
            buf.append("        ").append("</foreach>").append(newLine);
            buf.append("    ").append("</insert>").append(newLine);
        }
        {
            // updateByPrimaryKeySelective
            buf.append("    ").append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("UPDATE ").append(tableName).append(newLine);
            int i = 0;
            buf.append("        ").append("<set>").append(newLine);
            for (String column : columns) {
                if (tableInfo.getPrimaryKeys().contains(column)) {
                    i++;
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(newLine);
                buf.append("                ").append(caseColumn).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                buf.append(",");
                buf.append(newLine);
                buf.append("            ").append("</if>").append(newLine);
                i++;
            }
            buf.append("        ").append("</set>").append(newLine);
            buf.append("        ").append("WHERE ");
            i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</update>").append(newLine);
        }
        {
            // updateByPrimaryKey
            buf.append("    ").append("<update id=\"updateByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(newLine);
            buf.append("        ").append("UPDATE ").append(tableName).append(newLine);
            int i = 0;
            for (String column : columns) {
                if (tableInfo.getPrimaryKeys().contains(column)) {
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i == 0) {
                    buf.append("        ").append("SET ");
                } else {
                    buf.append(",").append(newLine).append("            ");
                }
                buf.append(caseColumn).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("        ").append("WHERE ");
            i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</update>").append(newLine);
        }
        buf.append("</mapper>");
        return buf.toString();
    }

    private static String caseDbSensitiveWords(String column) {
        if (column.equalsIgnoreCase("desc") || column.equalsIgnoreCase("order")) {
            return "`" + column + "`";
        } else {
            return column;
        }
    }

    private static String getJdbcTypeByJdbcTypeForSqlMap(String jdbcType) {
        if (jdbcType.equalsIgnoreCase("int")) {
            return "INTEGER";
        } else if (jdbcType.equalsIgnoreCase("datetime")) {
            return "TIMESTAMP";
        } else {
            return jdbcType;
        }
    }
}
