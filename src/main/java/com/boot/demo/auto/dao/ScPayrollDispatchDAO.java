package com.boot.demo.auto.dao;

import com.boot.demo.auto.entity.ScPayrollDispatch;

import java.util.List;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public interface ScPayrollDispatchDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(ScPayrollDispatch record);

    void insertSelective(ScPayrollDispatch record);

    void insertBatch(List<ScPayrollDispatch> records);

    ScPayrollDispatch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScPayrollDispatch record);

    int updateByPrimaryKey(ScPayrollDispatch record);
}