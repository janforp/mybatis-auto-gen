package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;

/**
 * Builder
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
@Slf4j
public class Builder {

    public static boolean codegenForOneTable(String oneTableName, DataSource dataSource) throws Exception {
        String sourcePath = EnvInfo.buildSourcePath();
        String sqlmapBasePath = EnvInfo.buildSqlmapBasePath();
        String fileCharset = EnvInfo.FILE_CHARSET;
        String modalPackage = EnvInfo.DATA_OBJECT_PACKAGE;
        String daoPackage = EnvInfo.DAO_PACKAGE;

        // dataObject
        String modalFilePath = sourcePath + modalPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getMobalNameByTableName(oneTableName) + ".java";
        // dao
        String daoFilePath = sourcePath + daoPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getDaoNameByTableName(oneTableName) + ".java";
        // mapper.xml
        String sqlMapperFilePath = sqlmapBasePath + MyBatisGenUtils.getSqlmapFileNameByTableName(oneTableName);

        try (Connection conn = dataSource.getConnection()) {
            // 表信息
            TableInfo tableInfo = TableInfoBuilder.getTableInfo(conn, EnvInfo.SCHEMA, oneTableName);

            tableInfo.getColumns().removeAll(EnvInfo.USE_DEFAULT_COLUMN_SET);

            if (tableInfo.getPrimaryKeys() == null || tableInfo.getPrimaryKeys().size() == 0) {
                log.error("[ERROR] " + oneTableName + " 没有主键，无法生成。");
                return false;
            }

            {
                if (new File(modalFilePath).exists()) {
                    log.error("[WARN] 实体类 " + MyBatisGenUtils.getMobalNameByTableName(oneTableName) + " 已存在，将会覆盖。");
                }
                String dataObject = DataObjectBuilder.buildDataObject(tableInfo, modalPackage);
                MyBatisGenUtils.writeText(new File(modalFilePath), dataObject, fileCharset);
            }

            {
                String mapperXml = SqlMapperBuilder.buildMapperXml(tableInfo, modalPackage);
                MyBatisGenUtils.writeText(new File(sqlMapperFilePath), mapperXml, fileCharset);
            }

            {
                if (!new File(daoFilePath).exists()) {
                    // 不存在才创建
                    String daoSource = DaoBuilder.buildDao(tableInfo, daoPackage, modalPackage);
                    MyBatisGenUtils.writeText(new File(daoFilePath), daoSource, fileCharset);
                }
            }
        }
        return true;
    }
}
