package com.boot.demo.auto.dao;

import com.boot.demo.auto.dataobject.IndexTestRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 索引测试相关
 *
 * * CREATE TABLE index_test_record
 * * (
 * *     `id`          BIGINT(20) AUTO_INCREMENT COMMENT 'id',
 * *     `name`        VARCHAR(32) DEFAULT NULL COMMENT '姓名',
 * *     `gender`      CHAR(1)      DEFAULT NULL COMMENT '性别(1:男, 2:女)',
 * *     `is_delete`   BIGINT(20)   DEFAULT 0                 NULL COMMENT '逻辑删除',
 * *     `creator_id`  VARCHAR(32)  DEFAULT NULL COMMENT '创建人id',
 * *     `modifier_id` VARCHAR(32)  DEFAULT NULL COMMENT '更新人id',
 * *     `create_date` DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
 * *     `modify_date` DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 * *     PRIMARY KEY (`id`),
 * *     KEY `idx_name` (`name`)
 * * ) ENGINE = InnoDB
 * *   COLLATE utf8mb4_unicode_ci
 * *   DEFAULT CHARSET = utf8mb4 COMMENT ='索引测试相关'
 *
 * -- type=ALL：全表扫描，遍历整张表去查询匹配的结果，不走索引。
 * -- type=index：使用索引覆盖，仅仅扫描索引树，比ALL要快。
 * -- type=range：使用索引进行范围查询时就会用到range访问方法。
 * -- key：实际使用到的索引，如果为NULL就是没使用索引。
 *
 * -- LIKE查询以%开头会导致全索引扫描或者全表扫描，如果没有索引覆盖的话，查询到的数据会回表，多了一次IO操作，
 * -- 当MySQL预估全表扫描或全索引扫描的时间比走索引花费的时间更少时，就不会走索引。有了索引覆盖就不需要回表了，减少了IO操作，花费的时间更少，所以就使用了索引。
 *
 * -- type:range
 * -- possible_keys:idx_name
 * -- key:idx_name
 * -- rows:19280
 * -- filtered:100
 * -- Extra:Using index condition
 * -- 说明这段是使用了索引的
 * EXPLAIN SELECT  * FROM test.index_test_record WHERE `name` LIKE '朱%';
 *
 * -- type:ALL
 * -- possible_keys:null
 * -- key:null
 * -- rows:992386
 * -- filtered:11.11
 * -- Extra:Using where
 * -- 说明这段是没使用了索引的
 * EXPLAIN SELECT * FROM test.index_test_record WHERE `name` LIKE '%朱%';
 *
 *
 * -- type:index
 * -- possible_keys:null
 * -- key:idx_name
 * -- rows:992386
 * -- filtered:11.11
 * -- Extra:Using where; Using index
 * -- 说明这段是使用了索引的, LIKE查询以%开头使用了索引的原因就是使用了索引覆盖!针对二级索引MySQL提供了一个优化技术。即从辅助索引中就可以得到查询的记录，就不需要回表再根据聚集索引查询一次完整记录。
 * EXPLAIN SELECT  `name` FROM test.index_test_record WHERE `name` LIKE '%朱%';
 *
 *
 * -- type:index
 * -- possible_keys:null
 * -- key:idx_name
 * -- rows:992386
 * -- filtered:11.11
 * -- Extra:Using where; Using index
 * -- 说明这段是使用了索引的
 * EXPLAIN SELECT  `id` , `name`  FROM index_test_record WHERE `name` LIKE '%朱%';
 *
 * -- type:ALL
 * -- possible_keys:null
 * -- key:null
 * -- rows:992386
 * -- filtered:11.11
 * -- Extra:Using where
 * -- 说明这段是没使用了索引的
 * EXPLAIN SELECT  `id` , `name` , `gender` FROM index_test_record WHERE `name` LIKE '%朱%';
 *
 * -- 总结
 * -- LIKE查询 以%开头不一定会让索引失效。如果查询的结果中只包含主键和索引字段则会使用索引，反之则不会。
 *
 * @author zhucj
 * @since 20221027
 */
@Repository
public interface IndexTestRecordDAO {

    /**
     * 添加
     *
     * @param indexTestRecord 记录
     */
    void insert(IndexTestRecord indexTestRecord);

    /**
     * 选择性添加
     *
     * @param indexTestRecord 记录
     */
    void insertSelective(IndexTestRecord indexTestRecord);

    /**
     * 批量添加
     *
     * @param indexTestRecordList 记录
     */
    void insertBatch(List<IndexTestRecord> indexTestRecordList);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 选择性修改
     *
     * @param indexTestRecord 记录
     */
    void updateByPrimaryKeySelective(IndexTestRecord indexTestRecord);

    /**
     * 根据主键修改
     *
     * @param indexTestRecord 记录
     */
    void updateByPrimaryKey(IndexTestRecord indexTestRecord);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 记录
     */
    IndexTestRecord getById(Long id);
}