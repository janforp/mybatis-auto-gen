package com.boot.demo.auto.dao;

import com.boot.demo.auto.entity.ScPayrollReference;

import java.util.List;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public interface ScPayrollReferenceDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(ScPayrollReference record);

    void insertSelective(ScPayrollReference record);

    void insertBatch(List<ScPayrollReference> records);

    ScPayrollReference selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScPayrollReference record);

    int updateByPrimaryKey(ScPayrollReference record);
}