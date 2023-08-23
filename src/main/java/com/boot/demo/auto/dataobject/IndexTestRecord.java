package com.boot.demo.auto.dataobject;

import lombok.Data;

/**
 * 索引测试相关
 *
 * @author zhucj
 * @since 20221027
 */
@Data
public class IndexTestRecord {

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别(1:男, 2:女)
     */
    private Integer gender;
}