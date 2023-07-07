package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfoConstants;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * SqlMapperMethodBuilder
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
public class SqlMapperMethodBuilder {

    private static boolean isAccountIdColumn(String column) {
        if (EnvInfoConstants.EXIST_CREATOR_ID && EnvInfoConstants.CREATOR_ID.equals(column)) {
            return true;
        }
        return EnvInfoConstants.EXIST_MODIFIER_ID && EnvInfoConstants.MODIFIER_ID.equals(column);
    }

    public static String buildInsertSelective(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insertSelective
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<insert id=\"insertSelective\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("INSERT INTO ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(EnvInfoConstants.NEW_LINE);
        for (String column : columns) {
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 主键是自增长的就不插入主键
                continue;
            }
            String caseColumn = caseDbSensitiveWords(column);
            String propertyName = MyBatisGenUtils.underlineToCamel(column);

            if (isAccountIdColumn(column)) {
                buf.append("            ").append(caseColumn).append(",").append(EnvInfoConstants.NEW_LINE);
                continue;
            }

            boolean isPk = tableInfo.getPrimaryKeys().contains(column);
            if (!isPk) {
                buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(EnvInfoConstants.NEW_LINE);
            }
            buf.append("                ").append(caseColumn);
            buf.append(",");
            buf.append(EnvInfoConstants.NEW_LINE);
            if (!isPk) {
                buf.append("            ").append("</if>").append(EnvInfoConstants.NEW_LINE);
            }
        }
        buf.append("        ").append("</trim>").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("VALUES").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(EnvInfoConstants.NEW_LINE);

        for (String column : columns) {
            if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys().contains(column)) {
                // 主键是自增长的就不插入主键
                continue;
            }
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));

            if (isAccountIdColumn(column)) {
                buf.append("            ").append(EnvInfoConstants.ACCOUNT).append(",").append(EnvInfoConstants.NEW_LINE);
                continue;
            }

            boolean isPk = tableInfo.getPrimaryKeys().contains(column);
            if (!isPk) {
                buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(EnvInfoConstants.NEW_LINE);
            }

            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                buf.append("                ").append(EnvInfoConstants.ACCOUNT);
            } else {
                buf.append("                ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }

            buf.append(",");
            buf.append(EnvInfoConstants.NEW_LINE);
            if (!isPk) {
                buf.append("            ").append("</if>").append(EnvInfoConstants.NEW_LINE);
            }
        }
        buf.append("        ").append("</trim>").append(EnvInfoConstants.NEW_LINE);

        buf.append("    ").append("</insert>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildInsertBatch(TableInfo tableInfo) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insertBatch
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<insert id=\"insertBatch\" parameterType=\"java.util.List\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ").append(EnvInfoConstants.NEW_LINE);
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
                    buf.append(EnvInfoConstants.NEW_LINE).append("        ");
                }
            }
            buf.append(caseColumn);
            i++;
        }
        buf.append(" )").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("VALUES").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" >").append(EnvInfoConstants.NEW_LINE);
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
                    buf.append(EnvInfoConstants.NEW_LINE).append("            ");
                }
            } else {
                buf.append(EnvInfoConstants.NEW_LINE).append("            ");
            }

            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                buf.append(EnvInfoConstants.ACCOUNT);
            } else {
                buf.append("#{item.").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("            ").append(")").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("</foreach>").append(EnvInfoConstants.NEW_LINE);
        buf.append("    ").append("</insert>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildUpdateByPrimaryKeySelective(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("UPDATE ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        int i = 0;
        buf.append("        ").append("<set>").append(EnvInfoConstants.NEW_LINE);
        for (String column : columns) {
            if (tableInfo.getPrimaryKeys().contains(column)) {
                i++;
                continue;
            }
            if (EnvInfoConstants.CREATOR_ID.equals(column)) {
                continue;
            }
            String caseColumn = caseDbSensitiveWords(column);
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));

            if (isAccountIdColumn(column)) {
                buf.append("            ").append(caseColumn).append(" = ").append(EnvInfoConstants.ACCOUNT).append(EnvInfoConstants.NEW_LINE);
                continue;
            }

            buf.append("            ").append("<if test=\"").append(propertyName).append(" != null\">").append(EnvInfoConstants.NEW_LINE);

            buf.append("                ").append(caseColumn).append(" = ");
            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                buf.append(EnvInfoConstants.ACCOUNT);
            } else {
                buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }
            buf.append(",");
            buf.append(EnvInfoConstants.NEW_LINE);
            buf.append("            ").append("</if>").append(EnvInfoConstants.NEW_LINE);
            i++;
        }
        buf.append("        ").append("</set>").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("WHERE ");
        i = 0;
        for (String column : tableInfo.getPrimaryKeys()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i > 0) {
                buf.append(EnvInfoConstants.NEW_LINE).append("        ").append("AND ");
            }
            buf.append(caseDbSensitiveWords(column)).append(" = ");
            buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        if (EnvInfoConstants.EXIST_IS_DELETE) {
            buf.append("        AND is_delete = 0").append(EnvInfoConstants.NEW_LINE);
        }
        buf.append("    ").append("</update>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildBaseAllColumn(TableInfo tableInfo) {
        List<String> columns = tableInfo.getColumns();
        StringBuilder buf = new StringBuilder(4096);
        // SELECT_All_Column
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<sql id=\"BASE_All_COLUMN\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ");
        int i = 0;
        for (String column : columns) {
            if (i > 0) {
                buf.append(", ");
                if (i % 5 == 0) {
                    buf.append(EnvInfoConstants.NEW_LINE).append("        ");
                }
            }
            buf.append(caseDbSensitiveWords(column));
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("    ").append("</sql>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildResultMap(TableInfo tableInfo, String modalName) {
        List<String> columns = tableInfo.getColumns();
        StringBuilder buf = new StringBuilder(4096);
        // BaseResultMap
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<resultMap id=\"BaseResultMap\" type=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        for (String column : columns) {
            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                continue;
            }
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            buf.append("        ");
            if (tableInfo.getPrimaryKeys().contains(column)) {
                buf.append("<id");
            } else {
                buf.append("<result");
            }

            buf.append(" column=\"").append(column).append("\" property=\"").append(propertyName).append("\" jdbcType=\"").append(jdbcType).append("\"/>").append(EnvInfoConstants.NEW_LINE);
        }
        buf.append("    ").append("</resultMap>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildGetById(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        // selectByPrimaryKey
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<select id=\"getById\" resultMap=\"BaseResultMap\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("SELECT ").append(EnvInfoConstants.NEW_LINE).append("        ").append("<include refid=\"BASE_All_COLUMN\"/>").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("FROM ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("WHERE ");
        int i = 0;
        for (String column : tableInfo.getPrimaryKeys()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i > 0) {
                buf.append(EnvInfoConstants.NEW_LINE).append("        ").append("AND ");
            }
            buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        if (EnvInfoConstants.EXIST_IS_DELETE) {
            buf.append("        AND is_delete = 0").append(EnvInfoConstants.NEW_LINE);
        }
        buf.append("    ").append("</select>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildDeleteByPrimaryKey(TableInfo tableInfo, String modalName) {
        if (EnvInfoConstants.EXIST_IS_DELETE) {
            return logicDelete(tableInfo, modalName);
        } else {
            return physicsDelete(tableInfo, modalName);
        }
    }

    private static String logicDelete(TableInfo tableInfo, String modalName) {
        // deleteByPrimaryKey
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<update id=\"deleteByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("UPDATE ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        buf.append("        SET is_delete = id,").append(EnvInfoConstants.NEW_LINE);
        if (EnvInfoConstants.EXIST_MODIFIER_ID) {
            buf.append("        ");
            buf.append(EnvInfoConstants.MODIFIER_ID).append(" = ").append(EnvInfoConstants.ACCOUNT).append(EnvInfoConstants.NEW_LINE);
        }
        buf.append("        ").append("WHERE ");
        int i = 0;
        for (String column : tableInfo.getPrimaryKeys()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i > 0) {
                buf.append(EnvInfoConstants.NEW_LINE).append("        ").append("AND ");
            }
            buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("        AND is_delete = 0").append(EnvInfoConstants.NEW_LINE);
        buf.append("    ").append("</update>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    private static String physicsDelete(TableInfo tableInfo, String modalName) {
        // deleteByPrimaryKey
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<delete id=\"deleteByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("DELETE FROM ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("WHERE ");
        int i = 0;
        for (String column : tableInfo.getPrimaryKeys()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i > 0) {
                buf.append(EnvInfoConstants.NEW_LINE).append("        ").append("AND ");
            }
            buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("    ").append("</delete>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildInsert(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();

        // insert
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<insert id=\"insert\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ").append(EnvInfoConstants.NEW_LINE);
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
                    buf.append(EnvInfoConstants.NEW_LINE).append("        ");
                }
            }
            buf.append(caseColumn);
            i++;
        }
        buf.append(" )").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("VALUES ( ").append(EnvInfoConstants.NEW_LINE).append("        ");
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
                    buf.append(EnvInfoConstants.NEW_LINE).append("        ");
                }
            }
            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                buf.append(EnvInfoConstants.ACCOUNT);
            } else {
                buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }
            i++;
        }
        buf.append(" )").append(EnvInfoConstants.NEW_LINE);
        buf.append("    ").append("</insert>").append(EnvInfoConstants.NEW_LINE);
        return buf.toString();
    }

    public static String buildUpdateByPrimaryKey(TableInfo tableInfo, String modalName) {
        StringBuilder buf = new StringBuilder(4096);
        List<String> columns = tableInfo.getColumns();
        String tableName = tableInfo.getTableName();
        buf.append(EnvInfoConstants.NEW_LINE).append("    ").append("<update id=\"updateByPrimaryKey\" parameterType=\"").append(modalName).append("\">").append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("UPDATE ").append(tableName).append(EnvInfoConstants.NEW_LINE);
        int i = 0;
        for (String column : columns) {
            if (tableInfo.getPrimaryKeys().contains(column)) {
                continue;
            }
            if (EnvInfoConstants.CREATOR_ID.equals(column)) {
                continue;
            }
            String caseColumn = caseDbSensitiveWords(column);
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i == 0) {
                buf.append("        ").append("SET ");
            } else {
                buf.append(",").append(EnvInfoConstants.NEW_LINE).append("            ");
            }
            buf.append(caseColumn).append(" = ");
            if (EnvInfoConstants.ACCOUNT_ID_COLUMN_SET.contains(column)) {
                buf.append(EnvInfoConstants.ACCOUNT);
            } else {
                buf.append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            }
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        buf.append("        ").append("WHERE ");
        i = 0;
        for (String column : tableInfo.getPrimaryKeys()) {
            String propertyName = MyBatisGenUtils.underlineToCamel(column);
            String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
            if (i > 0) {
                buf.append(EnvInfoConstants.NEW_LINE).append("        ").append("AND ");
            }
            buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
            i++;
        }
        buf.append(EnvInfoConstants.NEW_LINE);
        if (EnvInfoConstants.EXIST_IS_DELETE) {
            buf.append("        AND is_delete = 0").append(EnvInfoConstants.NEW_LINE);
        }
        buf.append("    ").append("</update>").append(EnvInfoConstants.NEW_LINE);
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
        } else if (jdbcType.equals("TEXT")) {
            return "VARCHAR";
        } else {
            return jdbcType;
        }
    }
}
