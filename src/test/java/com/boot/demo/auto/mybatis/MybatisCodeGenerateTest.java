package com.boot.demo.auto.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import com.boot.demo.auto.mybatis.util.DaoBuilder;
import com.boot.demo.auto.mybatis.util.DataObjectBuilder;
import com.boot.demo.auto.mybatis.util.MyBatisGenUtils;
import com.boot.demo.auto.mybatis.util.SqlMapperBuilder;
import com.boot.demo.auto.mybatis.util.TableInfoBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.sql.Connection;

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

    private static final String[] tableNames = new String[] { "ips_declaration_record", "cr_group" };

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
        EnvInfo envInfo = new EnvInfo();
        String sourcePath = EnvInfo.buildSourcePath();
        String sqlmapBasePath = EnvInfo.buildSqlmapBasePath();
        String schema = envInfo.getSchema();
        String fileCharset = envInfo.getFileCharset();
        String modalPackage = envInfo.getModalPackage();
        String daoPackage = envInfo.getDaoPackage();

        // dataObject
        String modalFilePath = sourcePath + modalPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getMobalNameByTableName(oneTableName) + ".java";
        // dao
        String daoFilePath = sourcePath + daoPackage.replace(".", File.separator) + File.separator + MyBatisGenUtils.getDaoNameByTableName(oneTableName) + ".java";
        // mapper.xml
        String sqlMapperFilePath = sqlmapBasePath + MyBatisGenUtils.getSqlmapFileNameByTableName(oneTableName);

        try (Connection conn = dataSource.getConnection()) {
            // 表信息
            TableInfo tableInfo = TableInfoBuilder.getTableInfo(conn, schema, oneTableName);
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
                    String daoSource = DaoBuilder.buildDao(tableInfo, daoPackage, modalPackage);
                    MyBatisGenUtils.writeText(new File(daoFilePath), daoSource, fileCharset);
                }
            }
        }
    }
}