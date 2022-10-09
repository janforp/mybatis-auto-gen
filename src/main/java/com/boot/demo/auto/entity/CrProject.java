package com.boot.demo.auto.entity;

import lombok.Data;

import java.sql.Date;

/**
 * @author zhucj
 * @since 20990909
 */
@Data
public class CrProject {

    /**
     * id
     */
    private Long id;

    /**
     * 分组CODE，如HR-PTS等
     */
    private String groupCode;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 逻辑删除
     */
    private Long isDelete;

    /**
     * 创建人
     */
    private String creatorId;

    /**
     * 修改人
     */
    private String modifierId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;
}