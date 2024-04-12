package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.util.EsopUseUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "SOPB_PRODUCTBOP")
public class ProductBopEntity {

    @Id
    @Column(name = "BOP_REV_UID")
    private String bopRevUid;

    @Column(name = "BOP_NAME")
    private String bopName;

    @Column(name = "BOP_ID")
    private String bopId;

    @Column(name = "BOP_UID")
    private String bopUid;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "PARTITION_NUM")
    private Integer partitionNum;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PUBLISH_DATE")
    private Long publishDate;

    @Column(name = "BOP_REV_ID")
    private String bopRevId;

    /***
     * 构建SOPB_PRODUCTBOP对象
     *
     * @param bopName 产品名称
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link String}
     */
    public static ProductBopEntity create(String bopName) {
        String generate = IdGeneratorUtil.generate();
        ProductBopEntity productBopEntity = new ProductBopEntity();
        productBopEntity.setBopRevUid(generate);
        productBopEntity.setBopName(bopName);
        productBopEntity.setBopId("00" + RandomUtil.randomInt(1000,9999));
        productBopEntity.setBopUid(generate);
        productBopEntity.setProductType(EsopConst.sopb_product_bop$product_type);
        productBopEntity.setPartitionNum(EsopConst.sopb_product_bop$partition_num);
        productBopEntity.setUserId(EsopConst.sopb_product_bop$user_id);
        productBopEntity.setPublishDate(System.currentTimeMillis());
        productBopEntity.setBopRevId(EsopUseUtil.randomStringUpper(1));
        return productBopEntity;
    }
}
