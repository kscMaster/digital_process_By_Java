package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.EsopOperationDB;
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
@Entity(name = "SOPB_OPERATION")
public class OperationEntity {

    @EmbeddedId
    private OperationEntity.OperationPk id;

    @Column(name = "OP_NAME")
    private String opName;

    @Column(name = "OP_COMPONENT")
    private String opComponent;

    @Column(name = "OP_NUM")
    private String opNum;

    @Column(name = "FIX_STATION")
    private String fixStation;

    @Column(name = "OP_TIME")
    private Integer opTime;

    @Column(name = "OP_ID")
    private String opId;

    @Column(name = "VOLUME_ID")
    private String volumeId;

    @Column(name = "KEY_OPERATION")
    private Integer keyOperation;

    @Column(name = "PARTITION_UID")
    private String partitionUid;

    @Column(name = "ATTENTION")
    private String attention;

    @Column(name = "FIX_RESOURCE")
    private String fixResource;

    @Column(name = "IS_PRECISE")
    private Integer isPrecise;

    @Column(name = "CONDITIONS")
    private String conditions;

    @Column(name = "OP_UID")
    private String opUid;

    @Column(name = "PROCESS_REV_UID")
    private String processRevUid;

    @Column(name = "OP_REV_ID")
    private String opRevId;

    @Column(name = "OP_GROUP")
    private String opGroup;

    @Column(name = "IS_COPY")
    private Integer isCopy;

    @Column(name = "OP_QTY")
    private Integer opQty;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class OperationPk implements Serializable {

        @Column(name = "OP_REV_UID")
        private String opRevUid;

        @Column(name = "BOP_REV_UID")
        private String bopRevUid;
    }
    /***
     * 构建SOPB_OPERATION对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link OperationEntity}
     */
    public static OperationEntity create(EsopOperationDB param) {
        OperationEntity operationEntity = new OperationEntity();
        OperationEntity.OperationPk operationPk = new OperationEntity.OperationPk();
        operationPk.setOpRevUid(param.getLezaoOpRevUid());
        operationPk.setBopRevUid(param.getEsopBopRevUid());
        operationEntity.setId(operationPk);
        operationEntity.setOpName(param.getLezaoOpName());
        operationEntity.setOpComponent(null);
        operationEntity.setOpNum(param.getLezaoOpNum());
        operationEntity.setFixStation(null);
        operationEntity.setOpTime(RandomUtil.randomInt(5, 100));
        operationEntity.setOpId(param.getLezaoItemId());
        operationEntity.setVolumeId(param.getEsopVolumeId());
        operationEntity.setKeyOperation(param.getLezaoKeyOperation());
        operationEntity.setPartitionUid(param.getEsopPartitionUid());
        operationEntity.setAttention(null);
        operationEntity.setFixResource(null);
        operationEntity.setIsPrecise(EsopConst.sopb_operation$is_precise);
        operationEntity.setConditions(null);
        operationEntity.setOpUid(param.getLezaoOpUid());
        operationEntity.setProcessRevUid(param.getLezaoProcessRevUid());
        operationEntity.setOpRevId(EsopUseUtil.versionChar(param.getLezaoRevisionId()));
        operationEntity.setOpGroup(null);
        operationEntity.setIsCopy(EsopConst.sopb_operation$is_copy);
        operationEntity.setOpQty(EsopConst.sopb_operation$op_qty);
        return operationEntity;
    }

    /***
     * 构建SOPB_OPERATION对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <OperationEntity>}
     */
    public static List<OperationEntity> create(List<EsopOperationDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(OperationEntity::create).collect(Collectors.toList());
    }
}
