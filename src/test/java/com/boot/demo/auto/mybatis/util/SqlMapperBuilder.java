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

    private static final String newLine = "\n";

    public static String buildSqlMapper(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        EnvInfo info = new EnvInfo();
        String daoName = info.getDaoPackage() + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
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
            buf.append(SqlMapperMethodBuilder.buildResultMap(tableInfo, modalName));
        }
        {
            // SELECT_All_Column
            buf.append(SqlMapperMethodBuilder.buildBaseAllColumn(tableInfo));
        }
        {
            // getById
            buf.append(SqlMapperMethodBuilder.buildGetById(tableInfo, modalName));
        }
        {
            // deleteByPrimaryKey
            buf.append(SqlMapperMethodBuilder.buildDeleteByPrimaryKey(tableInfo, modalName));
        }
        {
            // insert
            buf.append(SqlMapperMethodBuilder.buildInsert(tableInfo, modalName));
        }
        {
            // insertSelective
            buf.append(SqlMapperMethodBuilder.buildInsertSelective(tableInfo, modalName));
        }
        {
            buf.append(SqlMapperMethodBuilder.buildInsertBatch(tableInfo, modalName));
        }
        {
            // updateByPrimaryKeySelective
            buf.append(SqlMapperMethodBuilder.buildUpdateByPrimaryKeySelective(tableInfo, modalName));
        }
        {
            // updateByPrimaryKey
            buf.append(SqlMapperMethodBuilder.buildUpdateByPrimaryKey(tableInfo, modalName));
        }
        buf.append("</mapper>");
        return buf.toString();
    }
}
