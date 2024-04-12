package com.nancal.esop.entity;

import com.nancal.esop.db.ResourceDB;
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
@Entity(name = "SOPB_RESOURCE")
public class ResourceEntity {

    @Column(name = "RESOURCE_ID")
    private String resourceId;

    @Column(name = "RESOURCE_NAME")
    private String resourceName;

    @Id
    @Column(name = "RESOURCE_REV_UID")
    private String resourceRevUid;

    @Column(name = "SPEC")
    private String spec;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "VOLUME_ID")
    private String volumeId;

    @Column(name = "RESOURCE_REV_ID")
    private String resourceRevId;

    @Column(name = "RESOURCE_TYPE")
    private Integer resourceType;

    /***
     * 构建SOPB_RESOURCE对象
     *
     * @param param 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link ResourceEntity}
     */
    public static ResourceEntity create(ResourceDB param) {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setResourceId(param.getLezaoItemId());
        resourceEntity.setResourceName(param.getLezaoResourceName());
        resourceEntity.setResourceRevUid(param.getLezaoResourceRevUid());
        resourceEntity.setSpec(param.getLezaoSpec());
        resourceEntity.setRemark(param.getLezaoRemark());
        resourceEntity.setVolumeId(param.getEsopVolumeId());
        resourceEntity.setResourceRevId(EsopUseUtil.versionChar(param.getLezaoRevisionId()));
        resourceEntity.setResourceType(param.getEsopResourceType());
        return resourceEntity;
    }
    /***
     * 构建SOPB_RESOURCE对象
     *
     * @param params 参数
     * @author 徐鹏军
     * @date 2022/8/9 19:48
     * @return {@link List <ResourceEntity>}
     */
    public static List<ResourceEntity> create(List<ResourceDB> params) {
        return Optional.ofNullable(params)
                .orElse(Collections.emptyList())
                .stream()
                .map(ResourceEntity::create).collect(Collectors.toList());
    }
}
