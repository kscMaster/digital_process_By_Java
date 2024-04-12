package com.nancal.service.bo;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

@Data
@ApiModel(value = "Change 更改单 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Change extends WorkspaceObject {

    @ApiModelProperty(value = "更改单号")
    private String changeId;
    @ApiModelProperty(value = "更改意见")
    private String changeComment;


    /**
     * 获取与变更单相关的数据对象
     * @param objectType 关联表类型
     * @param id 变更单的idRequest
     * @author: 薛锦龙
     * @time: 2022/9/27
     * @return: {@link List< WorkspaceObjectResp>}
     */
    public List<WorkspaceObjectResp> getChangeObject(String objectType, IdRequest id){
        List<Triple<String, Ops,Object>> triples = new ArrayList<>();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        triples.add(Triple.of(RelationEntity.LEFT_OBJECT,Ops.EQ,id.getUid()));
        triples.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE,Ops.EQ,id.getObjectType()));
        List<WorkspaceObjectEntity> relationEntity = EntityUtil.getDynamicQuery(objectType, triples).fetch();
        if (CollUtil.isEmpty(relationEntity)){
            return Collections.emptyList();
        }
        List<RelationEntity> relationEntities = StreamEx.of(relationEntity).map(RelationEntity.class::cast).toList();
        List<WorkspaceObjectResp> resps = new ArrayList<>();
        relationEntities.forEach(data->{
            WorkspaceObjectEntity activeRevision = itemRevision.getActiveRevision(data.getRightObject(), data.getRightObjectType());
            WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(activeRevision.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(activeRevision,resp);
            resp.setLeftObject(data.getUid());
            resp.setLeftObjectType(data.getObjectType());
            resps.add(resp);
        });
        return resps;
    }

}