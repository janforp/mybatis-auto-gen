package com.boot.demo.auto.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import com.boot.demo.auto.mybatis.util.DataObjectBuilder;
import com.boot.demo.auto.mybatis.util.MyBatisGenUtils;
import com.boot.demo.auto.mybatis.util.SqlMapperBuilder;
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
                String sqlmapSource = SqlMapperBuilder.buildSqlMapper(tableInfo, modalPackage);
                MyBatisGenUtils.writeText(new File(sqlMapperFilePath), sqlmapSource, fileCharset);
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
