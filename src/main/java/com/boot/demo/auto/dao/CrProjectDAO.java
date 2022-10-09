package com.boot.demo.auto.dao;

import com.boot.demo.auto.entity.CrProject;

import java.util.List;

/**
 * @author zhucj
 * @since 20990909
 */
public interface CrProjectDAO {

    /**
     * 添加
     *
     * @param crProject 记录
     */
    void insert(CrProject crProject);

    /**
     * 选择性添加
     *
     * @param crProject 记录
     */
    void insertSelective(CrProject crProject);

    /**
     * 批量添加
     *
     * @param crProjectList 记录
     */
    void insertBatch(List<CrProject> crProjectList);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 选择性修改
     *
     * @param crProject 记录
     */
    void updateByPrimaryKeySelective(CrProject crProject);

    /**
     * 根据主键修改
     *
     * @param crProject 记录
     */
    void updateByPrimaryKey(CrProject crProject);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 记录
     */
    CrProject getById(Long id);

}