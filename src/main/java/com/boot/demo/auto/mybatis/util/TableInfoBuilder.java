package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfoConstants;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;

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
@UtilityClass
class TableInfoBuilder {

    private static String getTableComment(Connection conn, String schema, String tableName) throws SQLException {
        String sql = "SHOW create table " + schema + "." + tableName;
        ResultSet resultSet;
        String ddl = null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ddl = resultSet.getString("Create Table");
            }
            if (ddl == null) {
                return "";
            }
            int i = ddl.lastIndexOf("COMMENT=");
            if (i == -1) {
                return "";
            }
            ddl = ddl.substring(i);
            ddl = ddl.replace("COMMENT='", "");
            ddl = ddl.replace("'", "");
        }

        return ddl;
    }

    private Pair<Boolean, Boolean> fillMetaData(Connection conn, String tableName, String schema, List<String> columns,
            Map<String, String> columnCommentMap, Map<String, String> columnTypes, Map<String, Integer> columnSizes) throws SQLException {

        String sql = "SELECT * FROM " + schema + "." + tableName;
        // 是否需要导入包java.util.*
        boolean importUtil = false;
        // 是否需要导入包java.math.*
        boolean importMath = false;
        ResultSetMetaData resultSetMetaData;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            resultSetMetaData = preparedStatement.getMetaData();
            //统计列
            int size = resultSetMetaData.getColumnCount();
            String commentSql = "SELECT column_name,column_comment FROM information_schema.COLUMNS WHERE table_name='" + tableName + "' and table_schema = '" + schema + "'";
            ResultSet rs;
            try (PreparedStatement statement = conn.prepareStatement(commentSql)) {
                rs = statement.executeQuery();
                while (rs.next()) {
                    String columnName = rs.getString("column_name").toLowerCase();
                    String columnComment = rs.getString("column_comment");
                    if (columnComment != null && columnComment.length() > 0) {
                        columnCommentMap.put(columnName, columnComment);
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
                if ((colType.equalsIgnoreCase("datetime") || colType.equalsIgnoreCase("timestamp")) && !EnvInfoConstants.CREATE_UPDATE_DATE.contains(columnName)) {
                    importUtil = true;
                } else if (colType.equalsIgnoreCase("decimal")) {
                    importMath = true;
                }
            }
        }
        return new Pair<>(importUtil, importMath);
    }

    public static TableInfo getTableInfo(Connection conn, String schema, String tableName) throws SQLException {
        //查要生成实体类的表
        TableInfo tableInfo;
        List<String> columns = new ArrayList<>();
        Map<String, String> columnTypes = new HashMap<>();
        Map<String, Integer> columnSizes = new HashMap<>();
        Map<String, String> columnCommentMap = new HashMap<>();
        String tableComment = getTableComment(conn, schema, tableName);

        Pair<Boolean, Boolean> pair = fillMetaData(conn, tableName, schema, columns, columnCommentMap, columnTypes, columnSizes);
        // 是否需要导入包java.util.*
        boolean importUtil = pair.getKey();
        // 是否需要导入包java.math.*
        boolean importMath = pair.getValue();

        List<String> primaryKeys = new ArrayList<>();
        boolean primaryKeyAutoIncrement = false;
        String selectPrimaryKeysSql = "SELECT column_name FROM information_schema.KEY_COLUMN_USAGE WHERE constraint_name='PRIMARY' AND table_name='" + tableName + "' and table_schema = '" + schema + "'";
        ResultSet rs;
        try (PreparedStatement statement = conn.prepareStatement(selectPrimaryKeysSql)) {
            rs = statement.executeQuery();
            while (rs.next()) {
                String primaryKey = rs.getString("column_name");
                primaryKeys.add(primaryKey);
            }
        }

        if (primaryKeys.isEmpty()) {
            primaryKeys = null;
        }
        if (primaryKeys != null && primaryKeys.size() == 1) {
            // 有主键，且主键只有一个，才判断主键是否是自增长
            String selectPrimaryKeyAutoIncrementSql = "SELECT auto_increment FROM information_schema.TABLES WHERE table_name='" + tableName + "' and table_schema = '" + schema + "'";
            ResultSet resultSet;
            try (PreparedStatement prepareStatement = conn.prepareStatement(selectPrimaryKeyAutoIncrementSql)) {
                resultSet = prepareStatement.executeQuery();
                while (resultSet.next()) {
                    Object autoIncrementObj = resultSet.getObject("auto_increment");
                    if (autoIncrementObj != null) {
                        // 自增长主键
                        primaryKeyAutoIncrement = true;
                    }
                    break;
                }
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

        return tableInfo;
    }
}
