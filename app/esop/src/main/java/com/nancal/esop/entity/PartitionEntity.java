package com.nancal.esop.entity;

import com.nancal.common.utils.IdGeneratorUtil;
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
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity(name = "SOPB_PARTITION")
public class PartitionEntity {

    @EmbeddedId
    private PartitionPk id;

    @Column(name = "PARTITION_NAME")
    private String partitionName;

    @Column(name = "PARTITION_INDEX")
    private Integer partitionIndex;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class PartitionPk implements Serializable {

        @Column(name = "PARTITION_UID")
        private String partitionUid;

        @Column(name = "BOP_REV_UID")
        private String bopRvUid;
    }

    public static PartitionEntity create(String bopRevUid,String partitionName) {
        PartitionEntity partitionEntity = new PartitionEntity();
        PartitionEntity.PartitionPk partitionPk = new PartitionEntity.PartitionPk();
        partitionPk.setPartitionUid(IdGeneratorUtil.generate());
        partitionPk.setBopRvUid(bopRevUid);
        partitionEntity.setId(partitionPk);
        partitionEntity.setPartitionName(partitionName);
        partitionEntity.setPartitionIndex(0);
        return partitionEntity;
    }
}
