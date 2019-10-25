package com.boot.demo.auto.entity;

import java.util.*;
import java.math.*;

/**
 * Created by com.boot.demo.auto.MybatisCodeGenerate on 2019-10-25
 */
public class ScPayrollDispatch implements java.io.Serializable {

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
         * 工资表名称
    */
    private String payrollName;
        /** 
         * 工资条类型(1-引用算薪工资表, 2-导入工资表)
    */
    private Integer payrollType;
        /** 
         * 报表类型(导入工资表1-工资, 2-奖金, 3-报销)
    */
    private Integer salaryType;
        /** 
         * 引用算薪记录id
    */
    private Integer referenceId;
        /** 
         * 发薪人数
    */
    private Integer employeeCount;
        /** 
         * 应发金额合计
    */
    private BigDecimal payableTotal;
        /** 
         * 实发金额合计
    */
    private BigDecimal actualTotal;
        /** 
         * 消息内容
    */
    private String content;
        /** 
         * 是否隐藏空项(0-不隐藏, 1-隐藏)
    */
    private Integer hideEmpty;
        /** 
         * 发放时间
    */
    private Date dispatchDate;
        /** 
         * excel模板id
    */
    private String templateId;
        /** 
         * 工资条标题json
    */
    private String title;
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
    public ScPayrollDispatch() {
    }

    /**
     * full constructor
     */
    public ScPayrollDispatch(String appId, Long customerId, Integer deptId, Integer period, String payrollName, Integer payrollType, Integer salaryType, Integer referenceId, Integer employeeCount, BigDecimal payableTotal, BigDecimal actualTotal, String content, Integer hideEmpty, Date dispatchDate, String templateId, String title, Integer state, Integer isDelete, String creatorId, String modifierId, Date createDate, Date modifyDate) {
        this.appId = appId;
        this.customerId = customerId;
        this.deptId = deptId;
        this.period = period;
        this.payrollName = payrollName;
        this.payrollType = payrollType;
        this.salaryType = salaryType;
        this.referenceId = referenceId;
        this.employeeCount = employeeCount;
        this.payableTotal = payableTotal;
        this.actualTotal = actualTotal;
        this.content = content;
        this.hideEmpty = hideEmpty;
        this.dispatchDate = dispatchDate;
        this.templateId = templateId;
        this.title = title;
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
     * 工资表名称
     */
    public String getPayrollName() {
        return this.payrollName;
    }

    /**
     * 工资表名称
     */
    public void setPayrollName(String payrollName) {
        this.payrollName = payrollName;
    }

    /**
     * 工资条类型(1-引用算薪工资表, 2-导入工资表)
     */
    public Integer getPayrollType() {
        return this.payrollType;
    }

    /**
     * 工资条类型(1-引用算薪工资表, 2-导入工资表)
     */
    public void setPayrollType(Integer payrollType) {
        this.payrollType = payrollType;
    }

    /**
     * 报表类型(导入工资表1-工资, 2-奖金, 3-报销)
     */
    public Integer getSalaryType() {
        return this.salaryType;
    }

    /**
     * 报表类型(导入工资表1-工资, 2-奖金, 3-报销)
     */
    public void setSalaryType(Integer salaryType) {
        this.salaryType = salaryType;
    }

    /**
     * 引用算薪记录id
     */
    public Integer getReferenceId() {
        return this.referenceId;
    }

    /**
     * 引用算薪记录id
     */
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * 发薪人数
     */
    public Integer getEmployeeCount() {
        return this.employeeCount;
    }

    /**
     * 发薪人数
     */
    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    /**
     * 应发金额合计
     */
    public BigDecimal getPayableTotal() {
        return this.payableTotal;
    }

    /**
     * 应发金额合计
     */
    public void setPayableTotal(BigDecimal payableTotal) {
        this.payableTotal = payableTotal;
    }

    /**
     * 实发金额合计
     */
    public BigDecimal getActualTotal() {
        return this.actualTotal;
    }

    /**
     * 实发金额合计
     */
    public void setActualTotal(BigDecimal actualTotal) {
        this.actualTotal = actualTotal;
    }

    /**
     * 消息内容
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 是否隐藏空项(0-不隐藏, 1-隐藏)
     */
    public Integer getHideEmpty() {
        return this.hideEmpty;
    }

    /**
     * 是否隐藏空项(0-不隐藏, 1-隐藏)
     */
    public void setHideEmpty(Integer hideEmpty) {
        this.hideEmpty = hideEmpty;
    }

    /**
     * 发放时间
     */
    public Date getDispatchDate() {
        return this.dispatchDate;
    }

    /**
     * 发放时间
     */
    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    /**
     * excel模板id
     */
    public String getTemplateId() {
        return this.templateId;
    }

    /**
     * excel模板id
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * 工资条标题json
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 工资条标题json
     */
    public void setTitle(String title) {
        this.title = title;
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