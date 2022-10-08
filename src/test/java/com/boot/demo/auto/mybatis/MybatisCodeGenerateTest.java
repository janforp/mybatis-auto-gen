package com.boot.demo.auto.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import com.boot.demo.auto.mybatis.util.DataObjectBuilder;
import com.boot.demo.auto.mybatis.util.MyBatisGenUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成一张表的Modal / Dao / DaoImpl /sql**-mapper.xml
 *
 * @author Janita
 * @since 2015-09-12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisCodeGenerateTest {

    @Autowired(required = false)
    private DruidDataSource dataSource;

    private static final EnvInfo envInfo = new EnvInfo();

    private static final String[] tableNames = new String[] { "ips_declaration_record" };

    // 程序会自动找到basePath
    private static String basePath = System.getProperty("user.dir");

    private static final String DAO_PACKAGE = "com.boot.demo.auto.dao";

    @Test
    public void codeAutoGenerateTest() throws Exception {
        try {
            if (!basePath.endsWith(File.separator)) {
                basePath = basePath + File.separator;
            }
            for (String tableName : tableNames) {
                codegenForOneTable(tableName);
                System.out.println(tableName + " - 生成完毕！");
            }
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
        Assert.assertTrue(true);
    }

    public void codegenForOneTable(String oneTableName) throws Exception {
        String sourcePath = EnvInfo.buildSourcePath();
        String sqlmapBasePath = EnvInfo.buildSqlmapBasePath();
        String schema = envInfo.getSchema();
        String fileCharset = envInfo.getFileCharset();
        String modalPackage = envInfo.getModalPackage();
        String daoPackage = envInfo.getDaoPackage();

        String modalFilePath = sourcePath + modalPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getMobalNameByTableName(oneTableName) + ".java";
        String daoFilePath = sourcePath + daoPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getDaoNameByTableName(oneTableName) + ".java";

        String sqlMapperFilePath = sqlmapBasePath + MyBatisGenUtils.getSqlmapFileNameByTableName(oneTableName);
        String customSqlMapperFilePath = sqlmapBasePath + MyBatisGenUtils.getCustomSqlmapFileNameByTableName(oneTableName);
        try (Connection conn = dataSource.getConnection()) {
            TableInfo tableInfo = getTableInfo(conn, schema, oneTableName);
            if (tableInfo.getPrimaryKeys() == null || tableInfo.getPrimaryKeys().size() == 0) {
                System.err.println("[ERROR] " + oneTableName + " 没有主键，无法生成。");
                return;
            }

            {
                if (new File(modalFilePath).exists()) {
                    System.err.println("[WARN] 实体类 " + MyBatisGenUtils.getMobalNameByTableName(oneTableName) + " 已存在，将会覆盖。");
                }
                String modalSource = DataObjectBuilder.buildModal(tableInfo, modalPackage);
                MyBatisGenUtils.writeText(new File(modalFilePath), modalSource, fileCharset);
            }

            {
                String sqlmapSource = buildSqlMapper(tableInfo, modalPackage);
                MyBatisGenUtils.writeText(new File(sqlMapperFilePath), sqlmapSource, fileCharset);
            }

            {
                if (!new File(customSqlMapperFilePath).exists()) {
                    // 自定义SQL文件不存在,才创建
                    String customSqlmapSource = buildCustomSqlMapper(tableInfo, modalPackage);
                    MyBatisGenUtils.writeText(new File(customSqlMapperFilePath), customSqlmapSource, fileCharset);
                }
            }

            {
                if (!new File(daoFilePath).exists()) {
                    // 不存在才创建
                    String daoSource = buildDao(tableInfo, daoPackage, modalPackage);
                    MyBatisGenUtils.writeText(new File(daoFilePath), daoSource, fileCharset);
                }
            }
        }
    }

    private String buildDao(TableInfo tableInfo, String daoPackage, String modalPackage) {
        StringBuilder buf = new StringBuilder(1024);
        String tableName = tableInfo.getTableName();
        String daoName = MyBatisGenUtils.getDaoNameByTableName(tableName);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String modalNameWithPackage = modalPackage + "." + modalName;
        String newLine = "\n";
        buf.append("package ").append(daoPackage).append(";").append(newLine);
        buf.append(newLine);
        buf.append("import ").append(modalNameWithPackage).append(";").append(newLine);
        buf.append(newLine);
        buf.append("import java.util.List;").append(newLine);
        buf.append(newLine);
        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("public interface ").append(daoName).append(" {").append(newLine);
        {
            buf.append("    ").append("int deleteByPrimaryKey(");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");").append(newLine).append(newLine);
        }
        {

            buf.append("    ").append("void insert(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("void insertSelective(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("void insertBatch(List<").append(modalName).append("> records);").append(newLine).append(newLine);
        }
        {
            buf.append("    ").append(modalName).append(" selectByPrimaryKey(");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");")
                    .append(newLine).append(newLine);
        }
        {
            buf.append("    ").append("int updateByPrimaryKeySelective(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("int updateByPrimaryKey(").append(modalName).append(" record);").append(newLine);
        }

        buf.append("}");
        return buf.toString();
    }

    private String buildCustomSqlMapper(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String daoName = DAO_PACKAGE + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
        String newLine = "\n";
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(newLine);
        buf.append(
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >")
                .append(newLine);
        buf.append(
                "<mapper namespace=\"").append(daoName).append("\">")
                .append(newLine);
        buf.append("</mapper>");
        return buf.toString();
    }

    private String buildSqlMapper(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String daoName = DAO_PACKAGE + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
        String newLine = "\n";
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(newLine);
        buf.append(
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >")
                .append(newLine);
        buf.append(
                "<mapper namespace=\"").append(daoName).append("\">")
                .append(newLine);
        List<String> columns = tableInfo.getColumns();
        int columnsSize = columns.size();
        List<String> columnsIds = new ArrayList<String>(columnsSize);
        List<String> columnsNotIds = new ArrayList<String>(columnsSize);
        for (String column : columns) {
            if (tableInfo.getPrimaryKeys().contains(column)) {
                columnsIds.add(column);
            } else {
                columnsNotIds.add(column);
            }
        }
        {
            List<String> newColumnList = new ArrayList<String>(columnsSize);
            newColumnList.addAll(columnsIds);
            newColumnList.addAll(columnsNotIds);
            columns = newColumnList;
        }
        {
            // BaseResultMap
            buf.append("    ").append("<resultMap id=\"BaseResultMap\" type=\"").append(modalName)
                    .append(
                            "\">").append(newLine);
            int i = 0;

            for (String column : columns) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                buf.append("        ");
                if (tableInfo.getPrimaryKeys().contains(column)) {
                    buf.append("<id");
                } else {
                    buf.append("<result");
                }
                buf.append(" column=\"").append(column)
                        .append("\" property=\"").append(propertyName).append(
                        "\" jdbcType=\"").append(jdbcType).append("\"/>").append(newLine);
                i++;
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
            buf.append("    ").append(
                    "<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
            buf.append("        ").append("SELECT *")
                    .append(
                            newLine);
            buf.append("        ").append("FROM ").append(tableName).append(newLine);
            buf.append("        ").append("WHERE ");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</select>").append(newLine);
        }
        {
            // deleteByPrimaryKey
            buf.append("    ").append(
                    "<delete id=\"deleteByPrimaryKey\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
            buf.append("        ").append("DELETE FROM ").append(tableName).append(newLine);
            buf.append("        ").append("WHERE ");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</delete>").append(newLine);
        }
        String selectKeyBuf = null;
        {
            // insert
            buf.append("    ").append(
                    "<insert id=\"insert\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ");
            int i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
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
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
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
                selectKeyBufTmp.append("        ").append("<selectKey keyProperty=\"" + propertyName + "\" resultType=\"" + javaType + "\">").append(newLine);
                selectKeyBufTmp.append("            ").append("SELECT LAST_INSERT_ID() AS " + propertyName).append(newLine);
                selectKeyBufTmp.append("        ").append("</selectKey>").append(newLine);
                selectKeyBuf = selectKeyBufTmp.toString();
                buf.append(selectKeyBuf);
            }
            buf.append("    ").append("</insert>").append(newLine);
        }
        {
            // insertSelective
            buf.append("    ").append(
                    "<insert id=\"insertSelective\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(newLine);
            int i = 0;
            buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(newLine);
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    i++;
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                boolean isPk = tableInfo.getPrimaryKeys().contains(column);
                if (!isPk) {
                    buf.append("            ")
                            .append("<if test=\"" + propertyName + " != null\">")
                            .append(newLine);
                }
                buf.append("                ").append(caseColumn);
                buf.append(",");
                buf.append(newLine);
                if (!isPk) {
                    buf.append("            ").append("</if>").append(newLine);
                }
                i++;
            }
            buf.append("        ").append("</trim>").append(newLine);
            buf.append("        ").append("VALUES").append(newLine);
            i = 0;
            buf.append("        ").append("<trim prefix=\"(\" suffixOverrides=\",\" suffix=\")\">").append(newLine);
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    i++;
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
                boolean isPk = tableInfo.getPrimaryKeys().contains(column);
                if (!isPk) {
                    buf.append("            ")
                            .append("<if test=\"" + propertyName + " != null\">")
                            .append(newLine);
                }
                buf.append("                ").append("#{").append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                buf.append(",");
                buf.append(newLine);
                if (!isPk) {
                    buf.append("            ").append("</if>").append(newLine);
                }
                i++;
            }
            buf.append("        ").append("</trim>").append(newLine);
            if (selectKeyBuf != null && selectKeyBuf.length() > 0) {
                buf.append(selectKeyBuf);
            }
            buf.append("    ").append("</insert>").append(newLine);
        }
        {
            // insertBatch
            buf.append("    ").append(
                    "<insert id=\"insertBatch\" parameterType=\"java.util.List\">").append(newLine);
            buf.append("        ").append("INSERT INTO ").append(tableName).append(" ( ");
            int i = 0;
            for (String column : columns) {
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
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
                if (tableInfo.isPrimaryKeyAutoIncrement() && tableInfo.getPrimaryKeys()
                        .contains(column)) {
                    // 主键是自增长的就不插入主键
                    continue;
                }
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String jdbcType = getJdbcTypeByJdbcTypeForSqlMap(
                        tableInfo.getColumnTypes().get(column));
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
            buf.append("    ").append(
                    "<update id=\"updateByPrimaryKeySelective\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
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
                String
                        jdbcType =
                        getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                buf.append("            ")
                        .append("<if test=\"" + propertyName + " != null\">")
                        .append(newLine);
                buf.append("                ").append(caseColumn).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append(
                        "}");
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
                String
                        jdbcType =
                        getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</update>").append(newLine);
        }
        {
            // updateByPrimaryKey
            buf.append("    ").append(
                    "<update id=\"updateByPrimaryKey\" parameterType=\"")
                    .append(
                            modalName)
                    .append("\">").append(newLine);
            buf.append("        ").append("UPDATE ").append(tableName).append(newLine);
            int i = 0;
            for (String column : columns) {
                if (tableInfo.getPrimaryKeys().contains(column)) {
                    continue;
                }
                String caseColumn = caseDbSensitiveWords(column);
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String
                        jdbcType =
                        getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i == 0) {
                    buf.append("        ").append("SET ");
                } else {
                    buf.append(",").append(newLine).append("            ");
                }
                buf.append(caseColumn).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("        ").append("WHERE ");
            i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String
                        jdbcType =
                        getJdbcTypeByJdbcTypeForSqlMap(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(newLine).append("        ").append("AND ");
                }
                buf.append(caseDbSensitiveWords(column)).append(" = ").append("#{")
                        .append(propertyName).append(",jdbcType=").append(jdbcType).append("}");
                i++;
            }
            buf.append(newLine);
            buf.append("    ").append("</update>").append(newLine);
        }
        buf.append("</mapper>");
        //        System.out.println(buf);
        return buf.toString();
    }

    private String caseDbSensitiveWords(String column) {
        if (column.equalsIgnoreCase("desc") || column.equalsIgnoreCase("order")) {
            return "`" + column + "`";
        } else {
            return column;
        }
    }

    private String getJdbcTypeByJdbcTypeForSqlMap(String jdbcType) {
        if (jdbcType.equalsIgnoreCase("int")) {
            return "INTEGER";
        } else if (jdbcType.equalsIgnoreCase("datetime")) {
            return "TIMESTAMP";
        } else {
            return jdbcType;
        }
    }

    private TableInfo getTableInfo(Connection conn, String schema, String tableName) {
        //查要生成实体类的表
        String sql = "SELECT * FROM " + schema + "." + tableName;
        PreparedStatement preparedStatement;
        TableInfo tableInfo;
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();

            //统计列
            int size = resultSetMetaData.getColumnCount();
            List<String> columns = new ArrayList<>(size);
            Map<String, String> columnTypes = new HashMap<>(size);
            Map<String, Integer> columnSizes = new HashMap<>(size);
            Map<String, String> columnCommentMap = new HashMap<>(size);
            // 是否需要导入包java.util.*
            boolean importUtil = false;
            // 是否需要导入包java.sql.*
            boolean importSql = false;
            // 是否需要导入包java.math.*
            boolean importMath = false;
            {
                String commentSql = "SELECT column_name,column_comment FROM information_schema.COLUMNS WHERE table_name='" + tableName + "' and table_schema = '" + schema + "'";
                PreparedStatement statement = conn.prepareStatement(commentSql);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    String column_name = rs.getString("column_name").toLowerCase();
                    String column_comment = rs.getString("column_comment");
                    if (column_comment != null && column_comment.length() > 0) {
                        columnCommentMap.put(column_name, column_comment);
                    }
                }
            }

            for (int i = 0; i < size; i++) {
                String columnName = resultSetMetaData.getColumnName(i + 1).toLowerCase();
                columns.add(columnName);
                String colType = resultSetMetaData.getColumnTypeName(i + 1);
                colType = colType.replace("UNSIGNED", "").replace("unsigned", "").trim().toUpperCase();
                columnTypes.put(columnName, colType);
                int colSize = resultSetMetaData.getColumnDisplaySize(i + 1);
                columnSizes.put(columnName, colSize);
                if (colType.equalsIgnoreCase("datetime") || colType.equalsIgnoreCase("timestamp")) {
                    importUtil = true;
                } else if (colType.equalsIgnoreCase("decimal")) {
                    importMath = true;
                }
            }
            List<String> primaryKeys;
            boolean primaryKeyAutoIncrement = false;
            {
                String selectPrimaryKeysSql = "SELECT column_name FROM information_schema.KEY_COLUMN_USAGE WHERE constraint_name='PRIMARY' AND table_name='" + tableName + "' and table_schema = '" + schema + "'";
                PreparedStatement statement = conn.prepareStatement(selectPrimaryKeysSql);
                ResultSet rs = statement.executeQuery();
                primaryKeys = new ArrayList<>();
                while (rs.next()) {
                    String primaryKey = rs.getString("column_name");
                    primaryKeys.add(primaryKey);
                }
            }
            if (primaryKeys.size() <= 0) {
                primaryKeys = null;
            }
            if (primaryKeys != null && primaryKeys.size() == 1) {
                // 有主键，且主键只有一个，才判断主键是否是自增长
                String selectPrimaryKeyAutoIncrementSql = "SELECT auto_increment FROM information_schema.TABLES WHERE table_name='" + tableName + "' and table_schema = '" + schema + "'";
                PreparedStatement pStemt3 = conn.prepareStatement(selectPrimaryKeyAutoIncrementSql);
                ResultSet rs = pStemt3.executeQuery();
                while (rs.next()) {
                    Object autoIncrementObj = rs.getObject("auto_increment");
                    if (autoIncrementObj != null) {
                        // 自增长主键
                        primaryKeyAutoIncrement = true;
                    }
                    break;
                }
            }
            tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);
            tableInfo.setColumns(columns);
            tableInfo.setColumnTypes(columnTypes);
            tableInfo.setColumnSizes(columnSizes);
            tableInfo.setImportSql(importSql);
            tableInfo.setImportUtil(importUtil);
            tableInfo.setImportMath(importMath);
            tableInfo.setPrimaryKeyAutoIncrement(primaryKeyAutoIncrement);
            tableInfo.setPrimaryKeys(primaryKeys);
            tableInfo.setColumnCommentMap(columnCommentMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tableInfo;
    }
}
