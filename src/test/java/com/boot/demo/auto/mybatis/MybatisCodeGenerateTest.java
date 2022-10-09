package com.boot.demo.auto.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.demo.auto.mybatis.util.Builder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    @SuppressWarnings("all")
    public void codeAutoGenerateTest() throws Exception {
        try {
            for (String tableName : tableNames) {
                Builder.codegenForOneTable(tableName, dataSource);
                System.out.println(tableName + " - 生成完毕！");
            }
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
        boolean closed = dataSource.isClosed();
        Assert.assertTrue(closed);
    }
}