package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.Set;

/**
 * SqlMapperBuilder
 *
 * @author zhucj
 * @since 20220825
 */
class SqlMapperBuilder {

    public static String buildMapperXml(TableInfo tableInfo, String modalPackage, Set<String> useDefaultColumnSet, Set<String> accountIdSet) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String daoName = EnvInfo.daoPackage + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(EnvInfo.newLine);
        buf.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append(EnvInfo.newLine);
        buf.append("<mapper namespace=\"").append(daoName).append("\">").append(EnvInfo.newLine);
        {
            // BaseResultMap
            buf.append(SqlMapperMethodBuilder.buildResultMap(tableInfo, modalName));
            // SELECT_All_Column
            buf.append(SqlMapperMethodBuilder.buildBaseAllColumn(tableInfo));
        }

        tableInfo.getColumns().removeAll(useDefaultColumnSet);

        {
            // insert
            buf.append(SqlMapperMethodBuilder.buildInsert(tableInfo, modalName, accountIdSet));
            // insertSelective
            buf.append(SqlMapperMethodBuilder.buildInsertSelective(tableInfo, modalName, accountIdSet));
            // 批量添加
            buf.append(SqlMapperMethodBuilder.buildInsertBatch(tableInfo, accountIdSet));
        }

        {
            // deleteByPrimaryKey
            buf.append(SqlMapperMethodBuilder.buildDeleteByPrimaryKey(tableInfo, modalName));
        }
        {
            // updateByPrimaryKeySelective
            buf.append(SqlMapperMethodBuilder.buildUpdateByPrimaryKeySelective(tableInfo, modalName));
            // updateByPrimaryKey
            buf.append(SqlMapperMethodBuilder.buildUpdateByPrimaryKey(tableInfo, modalName));
        }
        {
            // getById
            buf.append(SqlMapperMethodBuilder.buildGetById(tableInfo, modalName));
        }

        buf.append("</mapper>");
        return buf.toString();
    }
}
