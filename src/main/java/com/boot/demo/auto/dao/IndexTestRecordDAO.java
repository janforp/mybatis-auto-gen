package com.boot.demo.auto.dao;

import com.boot.demo.auto.dataobject.IndexTestRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 索引测试相关
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