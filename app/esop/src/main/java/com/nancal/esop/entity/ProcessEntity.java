package com.nancal.esop.entity;

import com.nancal.esop.db.EsopProcessDB;
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
@Entity(name = "SOPB_PROCESS")
public class ProcessEntity {

    @EmbeddedId
    private ProcessPk id;

    @Column(name = "PROCESS_UID")
    private String processUid;

    @Column(name = "PROCESS_NUM")
    private String processNum;

    @Column(name = "PROCESS_ID")
    private String processId;

    @Column(name = "PROCESS_NAME")
    private String processName;

    @Column(name = "PROCESS_REV_ID")
    private String processRevId;

    @Column(name = "PROCESS_TIME")
    private String processTime;

    @Column(name = "PARTITION_UID")
    private String partitionUid;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class ProcessPk implements Serializable {

        @Column(name = "PROCESS_REV_UID")
        private String processRevUid;

        @Column(name = "BOP_REV_UID")
        private String bopRvUid;
    }

    /***
     * 构建SOPB_PROCESS对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link ProcessEntity}
     */
    public static ProcessEntity create(EsopProcessDB param) {
        ProcessEntity processEntity = new ProcessEntity();
        ProcessEntity.ProcessPk processPk = new ProcessEntity.ProcessPk();
        processPk.setProcessRevUid(param.getLezaoProcessRevUid());
        processPk.setBopRvUid(param.getEsopBopRevId());
        processEntity.setId(processPk);
        processEntity.setProcessUid(param.getLezaoProcessUid());
        processEntity.setProcessNum(param.getLezaoProcessNum());
        processEntity.setProcessId(param.getLezaoItemId());
        processEntity.setProcessName(param.getLezaoProcessName());
        processEntity.setProcessRevId(EsopUseUtil.versionChar(param.getLezaoRevisionId()));
        processEntity.setProcessTime(null);
        processEntity.setPartitionUid(param.getEsopPartitionUid());
        return processEntity;
    }

    /***
     * 构建SOPB_PROCESS对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List<ProcessEntity>}
     */
    public static List<ProcessEntity> create(List<EsopProcessDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(data -> create(data)).collect(Collectors.toList());
    }
}
