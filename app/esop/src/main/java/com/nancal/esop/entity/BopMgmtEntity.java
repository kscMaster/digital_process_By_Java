package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.EsopBopMgmtDB;
import io.swagger.annotations.ApiModelProperty;
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
@Entity(name = "SOPB_BOP_MGMT")
public class BopMgmtEntity {

    @EmbeddedId
    private BopMgmtEntity.BopMgmtPk id;

    @ApiModelProperty("产品组件id")
    @Column(name = "BOP_UID")
    private String bopUid;

    @ApiModelProperty("最后下载用户id")
    @Column(name = "LAST_DOWNLOAD_USERID")
    private Long lastDownloadUserId;

    @ApiModelProperty("最后下载时间")
    @Column(name = "LAST_DOWNLOAD_TIME")
    private Long lastDownloadTime;

    @ApiModelProperty("审批id")
    @Column(name = "QUALITY_ID")
    private Long qualityId;

    @ApiModelProperty("状态")
    @Column(name = "BOP_STATE")
    private Integer bopState;

    @ApiModelProperty("生命周期")
    @Column(name = "LIFECYCLE")
    private Integer lifecycle;

    @ApiModelProperty("索引")
    @Column(name = "BOP_INDEX")
    private Long bopIndex;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class BopMgmtPk implements Serializable {

        @Column(name = "BOP_REV_UID")
        private String bopRevUid;

        @Column(name = "SITE_NAME")
        private String siteName;
    }

    /***
     * 构建SOPB_BOP_MGMT对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link BopMgmtEntity}
     */
    public static BopMgmtEntity create(EsopBopMgmtDB param) {
        BopMgmtEntity bopMgmtEntity = new BopMgmtEntity();
        BopMgmtEntity.BopMgmtPk bopMgmtPk = new BopMgmtEntity.BopMgmtPk();
        bopMgmtPk.setBopRevUid(param.getBopRevUid());
        bopMgmtPk.setSiteName(EsopConst.sopb_bop_mgmt$sit_name);
        bopMgmtEntity.setId(bopMgmtPk);
        bopMgmtEntity.setBopUid(param.getBopUid());
        bopMgmtEntity.setLastDownloadUserId(null);
        bopMgmtEntity.setLastDownloadTime(null);
        bopMgmtEntity.setQualityId(null);
        bopMgmtEntity.setBopState(EsopConst.sopb_bop_mgmt$bop_state);
        bopMgmtEntity.setLifecycle(param.getLifecycle());
        bopMgmtEntity.setBopIndex(RandomUtil.randomLong(2));
        return bopMgmtEntity;
    }
    /***
     * 构建SOPB_BOP_MGMT对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <BopMgmtEntity>}
     */
    public static List<BopMgmtEntity> create(List<EsopBopMgmtDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(BopMgmtEntity::create).collect(Collectors.toList());
    }
}
