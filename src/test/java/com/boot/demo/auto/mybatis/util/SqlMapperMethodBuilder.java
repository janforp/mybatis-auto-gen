package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.List;
import java.util.Set;

/**
 * SqlMapperMethodBuilder
 *
 * @author zhucj
 * @since 20220825
 */
public class SqlMapperMethodBuilder {

    // TODO 可以改
    private static final String ACCOUNT = "'${@cn.com.servyou.hrbase.dao.util.AccountIdUtils@getAccountId()}'";

    private static final String newLine = "\n";

    public static String buildInsertSelective(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insertSelective
        buf.append(newLine).append("    ").append("<insert id=\"insertSelective\" parameterType=\"").append(modalName).append("\">").append(newLine);
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
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
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

        buf.append("    ").append("</insert>").append(newLine);
        return buf.toString();
    }

    public static String buildInsertBatch(TableInfo tableInfo) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insertBatch
        buf.append(newLine).append("    ").append("<insert id=\"insertBatch\" parameterType=\"java.util.List\">").append(newLine);
        buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ").append(newLine);
        buf.append("        ");
        int i = 0;
        for (String column : columns) {
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 主键是自增长的就不插入主键
                continue;
            }
            String caseColumn = caseDbSensitiveWords(column);
            if (i > 0) {
                buf.append(", ");
                if (i % 3 == 0) {
                    buf.append(newLine).append("        ");
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
                if (i % 3 == 0) {
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
        return buf.toString();
    }

    public static String buildUpdateByPrimaryKeySelective(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        buf.append(newLine).append("    ").append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"").append(modalName).append("\">").append(newLine);
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
        return buf.toString();
    }

    public static String buildBaseAllColumn(TableInfo tableInfo) {
        List<String> columns = tableInfo.getColumns();
        StringBuilder buf = new StringBuilder(4096);
        // SELECT_All_Column
        buf.append(newLine).append("    ").append("<sql id=\"BASE_All_COLUMN\">").append(newLine);
        buf.append("        ");
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
        return buf.toString();
    }

    public static String buildResultMap(TableInfo tableInfo, String modalName) {
        List<String> columns = tableInfo.getColumns();
        StringBuilder buf = new StringBuilder(4096);
        // BaseResultMap
        buf.append(newLine).append("    ").append("<resultMap id=\"BaseResultMap\" type=\"").append(modalName).append("\">").append(newLine);
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
        return buf.toString();
    }

    public static String buildGetById(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        // selectByPrimaryKey
        buf.append(newLine).append("    ").append("<select id=\"getById\" resultMap=\"BaseResultMap\" parameterType=\"").append(modalName).append("\">").append(newLine);
        buf.append("        ").append("SELECT ").append(newLine).append("        ").append("<include refid=\"BASE_All_COLUMN\"/>").append(newLine);
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
        return buf.toString();
    }

    public static String buildDeleteByPrimaryKey(TableInfo tableInfo, String modalName) {
        // deleteByPrimaryKey
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        buf.append(newLine).append("    ").append("<delete id=\"deleteByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(newLine);
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
        return buf.toString();
    }

    public static String buildInsert(TableInfo tableInfo, String modalName, Set<String> accountIdSet) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insert
        buf.append(newLine).append("    ").append("<insert id=\"insert\" parameterType=\"").append(modalName).append("\">").append(newLine);
        buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ").append(newLine);
        buf.append("        ");
        int i = 0;
        for (String column : columns) {
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 主键是自增长的就不插入主键
                continue;
            }
            String caseColumn = caseDbSensitiveWords(column);
            if (i > 0) {
                buf.append(", ");
                if (i % 3 == 0) {
                    buf.append(newLine).append("        ");
                }
            }
            buf.append(caseColumn);
            i++;
        }
        buf.append(" )").append(newLine);
        buf.append("        ").append("VALUES ( ").append(newLine).append("        ");
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
                if (i % 3 == 0) {
                    buf.append(newLine).append("        ");
                }
            }
            if (accountIdSet.contains(column)) {
                buf.append(ACCOUNT);
            } else {
                buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }
            i++;
        }
        buf.append(" )").append(newLine);
        buf.append("    ").append("</insert>").append(newLine);
        return buf.toString();
    }

    public static String buildUpdateByPrimaryKey(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();
        buf.append(newLine).append("    ").append("<update id=\"updateByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(newLine);
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
        } else if (jdbcType.equals("MEDIUMTEXT")) {
            return "VARCHAR";
        } else {
            return jdbcType;
        }
    }
}
