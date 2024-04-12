package com.nancal.esop.entity;

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
@Entity(name = "SOPB_PART_BOM")
public class PartBomEntity {

    @EmbeddedId
    private PartBomEntity.PartBomPk id;

    @Column(name = "LINK_DESIGN")
    private String linkDesign;

    @Column(name = "VOLUME_ID")
    private String volumeId;

    @Column(name = "REMOVED")
    private Integer removed;

    @Column(name = "IGNORED")
    private Integer ignored;

    @Column(name = "PARENT")
    private String parent;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class PartBomPk implements Serializable {

        @Column(name = "PRODUCTMODEL")
        private String productModel;

        @Column(name = "PART_NO")
        private String partNo;
    }

    /***
     * 构建SOPB_PART_BOM对象
     *
     * @param itemId 图代号
     * @param linkDesign 数模版本id
     * @param volumeId 数据集id
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link PartBomEntity}
     */
    public static PartBomEntity create(String itemId,String linkDesign,String volumeId,String productModel) {
        PartBomEntity partBomEntity = new PartBomEntity();
        PartBomEntity.PartBomPk partBomPk = new PartBomEntity.PartBomPk();
        partBomPk.setProductModel(productModel);
        partBomPk.setPartNo(itemId);
        partBomEntity.setId(partBomPk);
        partBomEntity.setLinkDesign(linkDesign);
        partBomEntity.setVolumeId(volumeId);
        partBomEntity.setRemoved(0);
        partBomEntity.setIgnored(0);
        partBomEntity.setParent(null);
        return partBomEntity;
    }
}
