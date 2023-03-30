package com.boot.demo.auto.dataobject;

import lombok.Data;

/**
 * 任务表
 *
 * @author zhucj
 * @since 20221027
 */
@Data
public class IpsTask {

    /**
     * id
     */
    private Long id;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * dept_id
     */
    private Long deptId;

    /**
     * iittask返回的请求id
     */
    private String requestId;

    /**
     * 任务类型代码
     */
    private String taskType;

    /**
     * 任务状态
     */
    private String state;

    /**
     * 结果json
     */
    private String resultJson;

    /**
     * json格式的参数
     */
    private String paramJson;

    /**
     * 任务结果代码
     */
    private String resultCode;

    /**
     * 结果信息
     */
    private String resultMessage;
}