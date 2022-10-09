package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import lombok.experimental.UtilityClass;

/**
 * SqlMapperBuilder
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
class SqlMapperBuilder {

    public static String buildMapperXml(TableInfo tableInfo, String modalPackage) {
        StringBuilder buf = new StringBuilder(4096);
        String tableName = tableInfo.getTableName();
        String modalName = modalPackage + "." + MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String daoName = EnvInfo.DAO_PACKAGE + "." + MyBatisGenUtils.getDaoNameByTableName(tableName);
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(EnvInfo.NEW_LINE);
        buf.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append(EnvInfo.NEW_LINE);
        buf.append("<mapper namespace=\"").append(daoName).append("\">").append(EnvInfo.NEW_LINE);
        {
            // BaseResultMap
            buf.append(SqlMapperMethodBuilder.buildResultMap(tableInfo, modalName));
            // BASE_All_COLUMN
            buf.append(SqlMapperMethodBuilder.buildBaseAllColumn(tableInfo));
        }

        {
            // insert
            buf.append(SqlMapperMethodBuilder.buildInsert(tableInfo, modalName));
            // insertSelective
            buf.append(SqlMapperMethodBuilder.buildInsertSelective(tableInfo, modalName));
            // 批量添加
            buf.append(SqlMapperMethodBuilder.buildInsertBatch(tableInfo));
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
