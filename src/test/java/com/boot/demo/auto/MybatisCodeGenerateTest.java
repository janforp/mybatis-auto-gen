package com.boot.demo.auto;

import com.alibaba.druid.pool.DruidDataSource;
import com.boot.demo.auto.mybatis.util.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * 生成一张表的Modal / Dao / DaoImpl /sql**-mapper.xml
 *
 * @author Janita
 * @since 2015-09-12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisCodeGenerateTest {

    private static final Set<String> tableNameSet = Sets.newLinkedHashSet(
            //            "cr_checklist",
            //            "cr_group",
            //            "cr_group_tester",
            //            "cr_project",
            //            "cr_project_tester",
            //            "cr_question",
            //            "cr_version_used_info",
            //            "ips_declaration_record",
            "cr_codereview"
    );

    @Autowired(required = false)
    private DruidDataSource dataSource;

    @Test
    public void codeAutoGenerateTest() throws Exception {
        try {
            for (String tableName : tableNameSet) {
                boolean codegen = Builder.codegenForOneTable(tableName, dataSource);
                Assert.assertTrue(codegen);
            }
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
        log.error(StringUtils.join(tableNameSet) + " 生成完毕！");
    }
}