package com.nancal.service.bo;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.model.entity.MfgTargetRLEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "MfgTargetRL 制造目标 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgTargetRL extends Relation  implements Serializable{

    /**
     * 通过左对象与左对象类型查找数据
     * @param leftObject
     * @param leftObjectType
     * @author: 薛锦龙
     * @time: 2022/7/19
     * @return: {@link WorkspaceObjectEntity}
     */
    public WorkspaceObjectEntity getByLeftObject(String leftObjectType, List<String> leftObject){
        List<Triple<String, Ops,Object>> triples = new ArrayList<>();
        triples.add(Triple.of(RelationEntity.LEFT_OBJECT,Ops.IN,leftObject));
        triples.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE,Ops.EQ,leftObjectType));
        WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(ReflectUtil.newInstance(MfgTargetRLEntity.class).getObjectType(), triples).fetchFirst();
        return workspaceObject;
    }

}