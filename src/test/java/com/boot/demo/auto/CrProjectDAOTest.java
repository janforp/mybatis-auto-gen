package com.boot.demo.auto;

import com.boot.demo.auto.dao.CrGroupDAO;
import com.boot.demo.auto.dataobject.CrGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * CrProjectDAOTest
 *
 * @author zhucj
 * @since 20220825
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrProjectDAOTest {

    @Autowired
    private CrGroupDAO crGroupDAO;

    @Test
    public void test() {
        CrGroup project = crGroupDAO.getById(1L);
        Assert.assertNotNull(project);
    }
}
