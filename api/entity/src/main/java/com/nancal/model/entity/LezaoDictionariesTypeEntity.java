//package com.nancal.model.entity;
//
//import lombok.Data;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * @author hewei
// * @date 2022/7/12 15:03
// * @Description
// */
//@Data
//@Entity(name = "lezao_dictionaries_type_copy1")
//public class LezaoDictionariesTypeEntity implements Serializable {
//    /**
//     * 主键id
//     */
//    @Id
//    private Long id;
//
//    /**
//     * 数据字典类型(1，列表 2，树)
//     */
//    private Integer dictType;
//
//    /**
//     * 数据字典名称
//     */
//    private String dictName;
//
//    /**
//     * 数据字典编码
//     */
//    private String dictCode;
//
//    /**
//     * 所属应用编码
//     */
//    private String appCode;
//
//    /**
//     * 租户id
//     */
//    private String tenantId;
//
//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
//    /**
//     * 创建人用户id
//     */
//    private String creationUserId;
//
//    /**
//     * 创建人用户名
//     */
//    private String creationUsername;
//
//    /**
//     * 更新时间
//     */
//    private LocalDateTime updateTime;
//
//    /**
//     * 更新人用户id
//     */
//    private String lastUpdateUserId;
//
//    /**
//     * 更新人用户名
//     */
//    private String lastUpdateUsername;
//
//    /**
//     * 是否删除 0未删除，1删除(默认0)
//     */
//    private Integer delFlag;
//}
