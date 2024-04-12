package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.GenerateFollowMsgBomReq;
import com.nancal.api.model.Gte4MfgProcessCloneReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.model.entity.MfgTargetRLEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface IGte4MfgProcessDomainService extends IMfgProcessDomainService{

   default BusinessObjectResp cloneProcess(Gte4MfgProcessCloneReq req){
        return null;
    }

    default BusinessObjectResp generateFollowMsgBom(GenerateFollowMsgBomReq req){
        return null;
    }

    /**
     *  判断该BOM行版本关系制造目标下是否存在对象
     *  1 不存在，弹出错误提示：当前对象未关联制造目标，请关联制造目标后再进行零件指派！
     *  2 存在，返回ebom入参（版本id+类型）
     * @param id
     * @author: 拓凯
     * @time: 2022/7/20
     * @return: {@link Object}
     */
    default Object findMfgTargetRL(IdRequest id){
        List<Triple<String, Ops,Object>> triples = new ArrayList<>();
        triples.add(Triple.of(MfgTargetRLEntity.LEFT_OBJECT,Ops.EQ,id.getUid()));
        triples.add(Triple.of(MfgTargetRLEntity.LEFT_OBJECT_TYPE,Ops.EQ,id.getObjectType()));
        WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(new MfgTargetRLEntity().getObjectType(), triples).fetchFirst();
        if (Objects.isNull(entity)) {
            return "当前对象未关联制造目标，请关联制造目标后再进行零件指派！";
        }
        return entity;
    }
}
