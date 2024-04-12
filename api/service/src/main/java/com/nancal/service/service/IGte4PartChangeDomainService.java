package com.nancal.service.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.base.IdRequest;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface IGte4PartChangeDomainService extends IGte4ChangeDomainService {
    default ChangeObjectResp getTabObject(IdRequest id){
        ChangeObjectResp resp = new ChangeObjectResp<>();
        IChangeAfterRLDomainService afterRLDomainService = SpringUtil.getBean(IChangeAfterRLDomainService.class);
        IChangeBeforeRLDomainService beforeRLDomainService = SpringUtil.getBean(IChangeBeforeRLDomainService.class);
        IChangeEffectRLDomainService effectRLDomainService = SpringUtil.getBean(IChangeEffectRLDomainService.class);
        resp.setEffectedList(effectRLDomainService.getEffectObject(id));
        resp.setBeforeList(beforeRLDomainService.getBeforeObject(id));
        resp.setAfterList(afterRLDomainService.getAfterObject(id));
        return resp;
    }
    WorkspaceObjectResp create(Gte4PartChangeReq req);

    TableResponse<WorkspaceObjectResp> find(TableRequest<Gte4PartChangeReq> req);

    BusinessObjectResp modify(Gte4PartChangeReq req);

    /**
     * 变更单获取页签属性
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/29
     * @return: {@link BusinessObjectResp}
     */
    default BusinessObjectResp getTabProperties(IdRequest id){
        Gte4ChangeResp object =(Gte4ChangeResp) IGte4ChangeDomainService.super.getObject(id);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<FileStorage> fileStorageByIn = itemRevision.getFileStorageNoRevision(object.getUid(),object.getObjectType());
        //获取附件
        if (CollUtil.isEmpty(fileStorageByIn)){
            return object;
        }
        List<FileAttrResp> fileAttrResps = fileStorageByIn.stream().map(resp -> {
                FileAttrResp fileAttrResp = new FileAttrResp();
                BeanUtil.copyPropertiesIgnoreNull(resp, fileAttrResp);
                fileAttrResp.setFileType(resp.getType());
                return fileAttrResp;
            }).collect(Collectors.toList());
        object.setFiles(fileAttrResps);
        return object;
    }

    default ChangeOrderResp getChangeOrder(IdRequest id) {
        return null;
    }


    default boolean saveChangeOrder(ChangeOrderReq req) {
        return true;
    }

    @Transactional
    default String deleteObjects(ValidList<IdRequest> ids){
        StringBuilder stringBuilder = new StringBuilder();
        ids.forEach(data->{
            try{
                //再删除更改单数据
                IGte4ChangeDomainService.super.deleteObject(data);
                //先根据更改单id查询出他所关联的更改后的和更改前的和受影响的数据并删除
                deleteSon(data,ChangeAfterRLEntity.LEFT_OBJECT,new ChangeAfterRLEntity().getObjectType());
                deleteSon(data,ChangeBeforeRLEntity.LEFT_OBJECT,new ChangeBeforeRLEntity().getObjectType());
                deleteSon(data,ChangeEffectRLEntity.LEFT_OBJECT,new ChangeEffectRLEntity().getObjectType());
            }catch (TipServiceException error){
                stringBuilder.append(error.getMessage());
            }
        });
        if (stringBuilder.length()==0){
            stringBuilder.append("true");
        }
        return stringBuilder.toString();
    }

    default void deleteSon(IdRequest date,String leftObject,String objectType) {
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(Triple.of(leftObject, Ops.EQ, date.getUid()));
        List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(objectType, paramsList).fetch();
        fetch.forEach(data->IGte4ChangeDomainService.super.deleteObject(new IdRequest(data.getUid(), objectType)));
    }
}
