package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
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

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "SOP_MO_MATERIAL")
public class MoMaterialEntity {

    @EmbeddedId
    private MoMaterialEntity.MoMaterialPk id;

    @Column(name = "MATERIAL_STATUS")
    private Integer materialStatus;

    @Column(name = "BOM_ITEM")
    private String bomItem;

    @Column(name = "ASSIGN_DESIGN")
    private String assignDesign;

    @Column(name = "CREATE_TIME")
    private Long createTime;

    @Column(name = "MATERIAL_DESC")
    private String materialDesc;

    @Column(name = "COMMON_DESIGN")
    private String commonDesign;

    @Column(name = "LOC_INFO")
    private String locInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class MoMaterialPk implements Serializable {

        @Column(name = "MOID")
        private String moId;

        @Column(name = "MATERIAL_NAME")
        private String materialName;

        @Column(name = "MATERIAL_QTY")
        private Integer materialQty;
    }

    /***
     * 构建SOP_MO_MATERIAL对象
     *
     * @param moId 主键
     * @param itemId 图代号
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link MoMaterialEntity}
     */
    public static MoMaterialEntity create(String moId,String itemId) {
        MoMaterialEntity moMaterialEntity = new MoMaterialEntity();
        MoMaterialEntity.MoMaterialPk moMaterialPk = new MoMaterialEntity.MoMaterialPk();
        moMaterialPk.setMoId(moId);
        moMaterialPk.setMaterialName(itemId);
        moMaterialPk.setMaterialQty(RandomUtil.randomInt(1,9));
        moMaterialEntity.setId(moMaterialPk);
        moMaterialEntity.setMaterialStatus(4);
        moMaterialEntity.setBomItem(null);
        moMaterialEntity.setAssignDesign(null);
        moMaterialEntity.setCreateTime(System.currentTimeMillis());
        moMaterialEntity.setMaterialDesc(null);
        moMaterialEntity.setCommonDesign(null);
        moMaterialEntity.setLocInfo(null);
        return moMaterialEntity;
    }
}
