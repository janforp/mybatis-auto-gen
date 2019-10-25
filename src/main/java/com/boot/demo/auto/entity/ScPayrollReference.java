package com.boot.demo.auto.entity;

import java.util.*;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public class ScPayrollReference implements java.io.Serializable {

    // Fields

    private Integer id;
        /** 
         * appId
    */
    private String appId;
        /** 
         * 企业id
    */
    private Long customerId;
        /** 
         * 部门id
    */
    private Integer deptId;
        /** 
         * 发薪月份
    */
    private Integer period;
        /** 
         * 报表类型
    */
    private String salaryType;
        /** 
         * 批次号(-1代表汇总)
    */
    private Integer batchNo;
        /** 
         * 状态(1-已发放, 2-已撤回)
    */
    private Integer state;
        /** 
         * 逻辑删除
    */
    private Integer isDelete;
        /** 
         * 创建人id
    */
    private String creatorId;
        /** 
         * 更新人id
    */
    private String modifierId;
        /** 
         * 创建时间
    */
    private Date createDate;
        /** 
         * 更新时间
    */
    private Date modifyDate;

    // Constructors

    /**
     * default constructor
     */
    public ScPayrollReference() {
    }

    /**
     * full constructor
     */
    public ScPayrollReference(String appId, Long customerId, Integer deptId, Integer period, String salaryType, Integer batchNo, Integer state, Integer isDelete, String creatorId, String modifierId, Date createDate, Date modifyDate) {
        this.appId = appId;
        this.customerId = customerId;
        this.deptId = deptId;
        this.period = period;
        this.salaryType = salaryType;
        this.batchNo = batchNo;
        this.state = state;
        this.isDelete = isDelete;
        this.creatorId = creatorId;
        this.modifierId = modifierId;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * appId
     */
    public String getAppId() {
        return this.appId;
    }

    /**
     * appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 企业id
     */
    public Long getCustomerId() {
        return this.customerId;
    }

    /**
     * 企业id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 部门id
     */
    public Integer getDeptId() {
        return this.deptId;
    }

    /**
     * 部门id
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * 发薪月份
     */
    public Integer getPeriod() {
        return this.period;
    }

    /**
     * 发薪月份
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * 报表类型
     */
    public String getSalaryType() {
        return this.salaryType;
    }

    /**
     * 报表类型
     */
    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    /**
     * 批次号(-1代表汇总)
     */
    public Integer getBatchNo() {
        return this.batchNo;
    }

    /**
     * 批次号(-1代表汇总)
     */
    public void setBatchNo(Integer batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 状态(1-已发放, 2-已撤回)
     */
    public Integer getState() {
        return this.state;
    }

    /**
     * 状态(1-已发放, 2-已撤回)
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 逻辑删除
     */
    public Integer getIsDelete() {
        return this.isDelete;
    }

    /**
     * 逻辑删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 创建人id
     */
    public String getCreatorId() {
        return this.creatorId;
    }

    /**
     * 创建人id
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 更新人id
     */
    public String getModifierId() {
        return this.modifierId;
    }

    /**
     * 更新人id
     */
    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * 创建时间
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 更新时间
     */
    public Date getModifyDate() {
        return this.modifyDate;
    }

    /**
     * 更新时间
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

}