package com.nancal.esop.entity;

import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.PartMaterialDB;
import com.nancal.esop.util.EsopUseUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "SOPB_PART_MATERIAL")
public class PartMaterialEntity {

    @EmbeddedId
    private PartMaterialEntity.PartMaterialPk id;

    @Column(name = "PART_NAME")
    private String partName;

    @Column(name = "PART_REV_ID")
    private String partRevId;

    @Column(name = "PART_REV_UID")
    private String partRevUid;

    @Column(name = "BASIC_NAME")
    private String basicName;

    @Column(name = "IGNORED")
    private Integer ignored;

    @Column(name = "LINK_DESIGN_UID")
    private String linkDesignUid;

    @Column(name = "VOLUME_ID")
    private String volumeId;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class PartMaterialPk implements Serializable {

        @Column(name = "PART_NO")
        private String partNo;

        @Column(name = "PRODUCT_TYPE")
        private String productType;
    }

    /***
     * 构建SOPB_PART_MATERIAL对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link PartMaterialEntity}
     */
    public static PartMaterialEntity create(PartMaterialDB param) {
        PartMaterialEntity partMaterialEntity = new PartMaterialEntity();
        PartMaterialEntity.PartMaterialPk partMaterialPk = new PartMaterialEntity.PartMaterialPk();
        partMaterialPk.setPartNo(param.getItemId());
        partMaterialPk.setProductType(EsopConst.sopb_product_bop$product_type);
        partMaterialEntity.setId(partMaterialPk);
        partMaterialEntity.setPartName(param.getPartName());
        partMaterialEntity.setPartRevId(EsopUseUtil.versionChar(param.getRevisionId()));
        partMaterialEntity.setPartRevUid(param.getPartRevUid());
        partMaterialEntity.setBasicName(EsopConst.BASIC_NAME);
        partMaterialEntity.setIgnored(0);
        partMaterialEntity.setLinkDesignUid(param.getLinkDesignUid());
        partMaterialEntity.setVolumeId(param.getVolumeId());
        return partMaterialEntity;
    }
    /***
     * 构建SOPB_PART_MATERIAL对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <PartMaterialEntity>}
     */
    public static List<PartMaterialEntity> create(List<PartMaterialDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(PartMaterialEntity::create).collect(Collectors.toList());
    }
}
