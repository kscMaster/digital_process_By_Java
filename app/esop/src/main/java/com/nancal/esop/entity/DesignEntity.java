package com.nancal.esop.entity;

import com.nancal.esop.db.EsopDesignDB;
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
@Entity(name = "SOPB_DESIGN")
public class DesignEntity {

    @Id
    @Column(name = "DESIGN_REV_UID")
    private String designRevUid;

    @Column(name = "DESIGN_UID")
    private String designUid;

    @Column(name = "DESIGN_ID")
    private String designId;

    @Column(name = "DESIGN_NAME")
    private String designName;

    @Column(name = "DESIGN_REV_ID")
    private String designRevId;

    @Column(name = "VOLUMEID")
    private String volumeId;

    /***
     * 构建SOPB_DESIGN对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link DesignEntity}
     */
    public static DesignEntity create(EsopDesignDB param) {
        DesignEntity designEntity = new DesignEntity();
        designEntity.setDesignRevUid(param.getLezaoDegignRevUid());
        designEntity.setDesignUid(param.getLezaoDesignUid());
        designEntity.setDesignId(param.getLezaoItemId());
        designEntity.setDesignName(param.getLezaoDesignName());
        designEntity.setDesignRevId(EsopUseUtil.versionChar(param.getLezaoRevisionId()));
        designEntity.setVolumeId(param.getEsopVolumeId());
        return designEntity;
    }
    /***
     * 构建SOPB_DESIGN对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <DesignEntity>}
     */
    public static List<DesignEntity> create(List<EsopDesignDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(data -> create(data)).collect(Collectors.toList());
    }
}
