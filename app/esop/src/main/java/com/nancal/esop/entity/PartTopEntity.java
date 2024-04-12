package com.nancal.esop.entity;

import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.PartTopDB;
import com.nancal.esop.util.EsopUseUtil;
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
@Entity(name = "SOPB_PART_TOP")
public class PartTopEntity {

    @Id
    @Column(name = "PRODUCTMODEL")
    private String productModel;

    @Column(name = "PARTID")
    private String partId;

    @Column(name = "PARTNAME")
    private String partName;

    @Column(name = "PARTUID")
    private String partUid;

    @Column(name = "PARTREV")
    private String partRev;

    @Column(name = "PARTREVUID")
    private String partRevUid;

    @Column(name = "BASICNAME")
    private String basicName;

    @Column(name = "LINKDESIGN")
    private String linkDesign;

    @Column(name = "VOLUMEID")
    private String volumeId;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    /***
     * 构建SOPB_PART_TOP对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link PartTopEntity}
     */
    public static PartTopEntity create(PartTopDB param) {
        PartTopEntity partTopEntity = new PartTopEntity();
        partTopEntity.setProductModel(param.getProductModule());
        partTopEntity.setPartId(param.getItemId());
        partTopEntity.setPartName(param.getPartName());
        partTopEntity.setPartUid(param.getPartUid());
        partTopEntity.setPartRev(EsopUseUtil.versionChar(param.getRevisionId()));
        partTopEntity.setPartRevUid(partTopEntity.getPartRevUid());
        partTopEntity.setBasicName(EsopConst.BASIC_NAME);
        partTopEntity.setLinkDesign(param.getLinkDesign());
        partTopEntity.setVolumeId(partTopEntity.getVolumeId());
        partTopEntity.setProductType(EsopConst.sopb_product_bop$product_type);
        return partTopEntity;
    }
    /***
     * 构建SOPB_PART_TOP对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <PartTopEntity>}
     */
    public static List<PartTopEntity> create(List<PartTopDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(PartTopEntity::create).collect(Collectors.toList());
    }
}
