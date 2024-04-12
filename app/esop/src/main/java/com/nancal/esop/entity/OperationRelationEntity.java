package com.nancal.esop.entity;

import com.nancal.esop.db.EsopOperationRelationDB;
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
@Embeddable
@Entity(name = "SOPB_OPERATION_RELATION")
public class OperationRelationEntity {

    @EmbeddedId
    private OperationRelationEntity.OperationRelationPk id;

    @Column(name = "RELATED_VALUE")
    private Integer relatedValue;

    @Column(name = "NOTE_IGNORED")
    private String noteIgnored;

    @Column(name = "NOTE_MAXQTY")
    private String noteMaxQty;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class OperationRelationPk implements Serializable {

        @Column(name = "OP_REV_UID")
        private String opRevUid;

        @Column(name = "BOP_REV_UID")
        private String bopRevUid;

        @Column(name = "RELATED_OBJECT")
        private String relatedObject;

        @Column(name = "RELATION_TYPE")
        private Integer relationType;
    }

    /***
     * 构建SOPB_OPERATION_RELATION对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link OperationRelationEntity}
     */
    public static OperationRelationEntity create(EsopOperationRelationDB param) {
        OperationRelationEntity operationRelationEntity = new OperationRelationEntity();
        OperationRelationEntity.OperationRelationPk relationPk = new OperationRelationEntity.OperationRelationPk();
        relationPk.setOpRevUid(param.getLezaoOpRevUid());
        relationPk.setBopRevUid(param.getEsopBopRevId());
        relationPk.setRelatedObject(param.getLezaoRelatedObject());
        relationPk.setRelationType(param.getEsopRelationType());
        operationRelationEntity.setId(relationPk);
        operationRelationEntity.setRelatedValue(null);
        operationRelationEntity.setNoteIgnored(null);
        operationRelationEntity.setNoteMaxQty(null);
        return operationRelationEntity;
    }
    /***
     * 构建SOPB_OPERATION_RELATION对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <OperationRelationEntity>}
     */
    public static List<OperationRelationEntity> create(List<EsopOperationRelationDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(data -> create(data)).collect(Collectors.toList());
    }
}
