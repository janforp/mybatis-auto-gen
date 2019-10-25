package com.boot.demo.auto.entity;

import java.util.*;
import java.math.*;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public class ScPayrollDispatchDetail implements java.io.Serializable {

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
         * 发放记录id
    */
    private Integer dispatchId;
        /** 
         * 员工id
    */
    private Integer employeeId;
        /** 
         * 应发金额
    */
    private BigDecimal payableAmount;
        /** 
         * 实发金额
    */
    private BigDecimal actualAmount;
        /** 
         * 是否已查看(0-未查看, 1-已查看)
    */
    private Integer isViewed;
        /** 
         * 查看时间
    */
    private Date viewDate;
        /** 
         * 工资条内容json
    */
    private String data;
        /** 
         * 状态(1-已发放, 2-已撤回)
    */
    private Integer status;
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
    public ScPayrollDispatchDetail() {
    }

    /**
     * full constructor
     */
    public ScPayrollDispatchDetail(String appId, Long customerId, Integer deptId, Integer period, Integer dispatchId, Integer employeeId, BigDecimal payableAmount, BigDecimal actualAmount, Integer isViewed, Date viewDate, String data, Integer status, Integer isDelete, String creatorId, String modifierId, Date createDate, Date modifyDate) {
        this.appId = appId;
        this.customerId = customerId;
        this.deptId = deptId;
        this.period = period;
        this.dispatchId = dispatchId;
        this.employeeId = employeeId;
        this.payableAmount = payableAmount;
        this.actualAmount = actualAmount;
        this.isViewed = isViewed;
        this.viewDate = viewDate;
        this.data = data;
        this.status = status;
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
     * 发放记录id
     */
    public Integer getDispatchId() {
        return this.dispatchId;
    }

    /**
     * 发放记录id
     */
    public void setDispatchId(Integer dispatchId) {
        this.dispatchId = dispatchId;
    }

    /**
     * 员工id
     */
    public Integer getEmployeeId() {
        return this.employeeId;
    }

    /**
     * 员工id
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 应发金额
     */
    public BigDecimal getPayableAmount() {
        return this.payableAmount;
    }

    /**
     * 应发金额
     */
    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    /**
     * 实发金额
     */
    public BigDecimal getActualAmount() {
        return this.actualAmount;
    }

    /**
     * 实发金额
     */
    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    /**
     * 是否已查看(0-未查看, 1-已查看)
     */
    public Integer getIsViewed() {
        return this.isViewed;
    }

    /**
     * 是否已查看(0-未查看, 1-已查看)
     */
    public void setIsViewed(Integer isViewed) {
        this.isViewed = isViewed;
    }

    /**
     * 查看时间
     */
    public Date getViewDate() {
        return this.viewDate;
    }

    /**
     * 查看时间
     */
    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    /**
     * 工资条内容json
     */
    public String getData() {
        return this.data;
    }

    /**
     * 工资条内容json
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * 状态(1-已发放, 2-已撤回)
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * 状态(1-已发放, 2-已撤回)
     */
    public void setStatus(Integer status) {
        this.status = status;
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