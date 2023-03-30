package com.boot.demo.auto.dao;

import com.boot.demo.auto.dataobject.IpsTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务表
 *
 * @author zhucj
 * @since 20221027
 */
@Repository
public interface IpsTaskDAO {

    /**
     * 添加
     *
     * @param ipsTask 记录
     */
    void insert(IpsTask ipsTask);

    /**
     * 选择性添加
     *
     * @param ipsTask 记录
     */
    void insertSelective(IpsTask ipsTask);

    /**
     * 批量添加
     *
     * @param ipsTaskList 记录
     */
    void insertBatch(List<IpsTask> ipsTaskList);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 选择性修改
     *
     * @param ipsTask 记录
     */
    void updateByPrimaryKeySelective(IpsTask ipsTask);

    /**
     * 根据主键修改
     *
     * @param ipsTask 记录
     */
    void updateByPrimaryKey(IpsTask ipsTask);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 记录
     */
    IpsTask getById(Long id);
}