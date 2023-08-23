package com.boot.demo.auto;

import com.boot.demo.auto.dao.IndexTestRecordDAO;
import com.boot.demo.auto.dataobject.IndexTestRecord;
import com.boot.demo.auto.faker.Faker;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * IndexTestRecordTest
 *
 * @author zhucj
 * @since 20230824
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IndexTestRecordTest {

    @Autowired
    private IndexTestRecordDAO indexTestRecordDAO;

    @Test
    public void batchInsertTest() {
        List<IndexTestRecord> indexTestRecordList = new ArrayList<>(999999);
        for (int i = 0; i < 999999; i++) {
            IndexTestRecord testRecord = new IndexTestRecord();
            testRecord.setGender(i % 2);
            testRecord.setName(Faker.name());

            indexTestRecordList.add(testRecord);
        }

        List<List<IndexTestRecord>> partition = Lists.partition(indexTestRecordList, 500);
        for (List<IndexTestRecord> list : partition) {
            indexTestRecordDAO.insertBatch(list);
        }
    }
}