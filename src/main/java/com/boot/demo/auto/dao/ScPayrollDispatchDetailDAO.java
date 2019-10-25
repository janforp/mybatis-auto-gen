package com.boot.demo.auto.dao;

import com.boot.demo.auto.entity.ScPayrollDispatchDetail;

import java.util.List;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public interface ScPayrollDispatchDetailDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(ScPayrollDispatchDetail record);

    void insertSelective(ScPayrollDispatchDetail record);

    void insertBatch(List<ScPayrollDispatchDetail> records);

    ScPayrollDispatchDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScPayrollDispatchDetail record);

    int updateByPrimaryKey(ScPayrollDispatchDetail record);
}