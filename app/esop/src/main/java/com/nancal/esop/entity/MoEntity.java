package com.nancal.esop.entity;

import cn.hutool.core.util.RandomUtil;
import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.MoDB;
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
@Entity(name = "SOP_MO")
public class MoEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "MO_NO")
    private String moNo;

    @Column(name = "MTM")
    private String mtm;

    @Column(name = "MTM_DESC")
    private String mtmDesc;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "MO_QTY")
    private Integer moQty;

    @Column(name = "UPDATE_TIMESTAMP")
    private Long updateTimestamp;

    @Column(name = "CURRENT_REVISION")
    private String currentRevision;

    @Column(name = "MO_ORIGIN")
    private Integer moOrigin;

    @Column(name = "MO_UPLOADID")
    private String moUploadId;

    @Column(name = "PRODUCT_MODEL")
    private String productModel;

    @Column(name = "DS_ID")
    private String dsId;

    @Column(name = "WORKING_DATE")
    private Long workingDate;

    @Column(name = "LINE_CODES")
    private String lineCodes;

    @Column(name = "CREATE_TIME")
    private Long createTime;

    @Column(name = "OFFSPEC_ID")
    private String offSpecId;

    @Column(name = "SO_NUM")
    private String soNum;

    @Column(name = "SO_ITEM")
    private String soItem;

    @Column(name = "LOGICAL_PLANT")
    private String logicalPlant;


    /***
     * 构建SOP_MO对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link MoEntity}
     */
    public static MoEntity create(MoDB param) {
        MoEntity moEntity = new MoEntity();
        moEntity.setId(param.getId());
        moEntity.setMoNo(param.getItemId());
        moEntity.setMtm(param.getItemId());
        moEntity.setMtmDesc(param.getMtmDesc());
        moEntity.setSiteName(EsopConst.sopb_bop_mgmt$sit_name);
        moEntity.setMoQty(RandomUtil.randomInt(1,300));
        moEntity.setUpdateTimestamp(System.currentTimeMillis());
        moEntity.setCurrentRevision(null);
        moEntity.setMoOrigin(4);
        moEntity.setMoUploadId(null);
        moEntity.setProductModel(param.getProductModule());
        moEntity.setDsId(null);
        moEntity.setWorkingDate(null);
        moEntity.setLineCodes(param.getLineCodes());
        moEntity.setCreateTime(System.currentTimeMillis());
        moEntity.setOffSpecId(null);
        moEntity.setSoNum(null);
        moEntity.setSoItem(null);
        moEntity.setLogicalPlant(null);
        return moEntity;
    }

    /***
     * 构建SOP_MO对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <MoEntity>}
     */
    public static List<MoEntity> create(List<MoDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(MoEntity::create).collect(Collectors.toList());
    }
}
