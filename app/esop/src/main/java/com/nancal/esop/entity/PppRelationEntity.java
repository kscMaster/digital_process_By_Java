package com.nancal.esop.entity;

import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.PppRelationDB;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "SOPB_PPP_RELATION")
public class PppRelationEntity {

    @Id
    @Column(name = "PPP_UID")
    private String pppUid;

    @Column(name = "BOP_UID")
    private String bopUid;

    @Column(name = "PRODUCT_MODEL")
    private String productModel;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "LINE_CODE")
    private String lineCode;

    @Column(name = "BOP_REV_UID")
    private String bopRevUid;

    /***
     * 构建SOPB_PPP_RELATION对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link PppRelationEntity}
     */
    public static PppRelationEntity create(PppRelationDB param) {
        PppRelationEntity pppRelationEntity = new PppRelationEntity();
        pppRelationEntity.setPppUid(param.getPppUid());
        pppRelationEntity.setBopUid(param.getBopUid());
        pppRelationEntity.setProductModel(param.getProductModel());
        pppRelationEntity.setSiteName(EsopConst.sopb_bop_mgmt$sit_name);
        pppRelationEntity.setLineCode(param.getLineCode());
        pppRelationEntity.setBopRevUid(param.getBopRevUid());
        return pppRelationEntity;
    }
    /***
     * 构建SOPB_PPP_RELATION对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <PppRelationEntity>}
     */
    public static List<PppRelationEntity> create(List<PppRelationDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(PppRelationEntity::create).collect(Collectors.toList());
    }
}
