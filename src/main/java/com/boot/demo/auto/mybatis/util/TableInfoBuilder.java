package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TableInfoBuilder
 *
 * @author zhucj
 * @since 20220825
 */
class TableInfoBuilder {

    private static String getTableComment(Connection conn, String schema, String tableName) throws SQLException {
        String sql = "SHOW create table " + schema + "." + tableName;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String ddl = null;
        while (resultSet.next()) {
            ddl = resultSet.getString("Create Table");
        }
        if (ddl == null) {
            return "";
        }
        int i = ddl.lastIndexOf("COMMENT=");
        ddl = ddl.substring(i);
        ddl = ddl.replace("COMMENT='", "");
        ddl = ddl.replace("'", "");
        return ddl;
    }

    public static TableInfo getTableInfo(Connection conn, String schema, String tableName) {

        //查要生成实体类的表
        String sql = "SELECT * FROM " + schema + "." + tableName;
        PreparedStatement preparedStatement;
        TableInfo tableInfo;
        try {
            String tableComment = getTableComment(conn, schema, tableName);

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
                if ((colType.equalsIgnoreCase("datetime") || colType.equalsIgnoreCase("timestamp")) && !EnvInfo.CREATE_UPDATE_DATE.contains(columnName)) {
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
            tableInfo.setImportSql(false);
            tableInfo.setImportUtil(importUtil);
            tableInfo.setImportMath(importMath);
            tableInfo.setPrimaryKeyAutoIncrement(primaryKeyAutoIncrement);
            tableInfo.setPrimaryKeys(primaryKeys);
            tableInfo.setColumnCommentMap(columnCommentMap);
            tableInfo.setTableComment(tableComment);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tableInfo;
    }
}
