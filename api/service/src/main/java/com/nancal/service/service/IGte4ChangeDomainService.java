package com.nancal.service.service;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.Gte4PartRevisionResp;
import com.nancal.api.model.Gte4RunTimeChangeAttachResp;
import com.nancal.api.model.ItemRevisionResp;
import com.nancal.api.model.RunTimeChangeAttachResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.model.entity.ChangeAfterRLEntity;
import com.nancal.model.entity.ChangeEntity;
import com.nancal.model.entity.Gte4DistOrgEntryEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.MasterRL;
import com.nancal.service.factory.MasterRLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface IGte4ChangeDomainService extends IChangeDomainService {

    Logger log = LoggerFactory.getLogger(IGte4ChangeDomainService.class);

    @Override
    default void fullOtherData(RunTimeChangeAttachResp resp, ItemRevisionResp beforeResp,
                               ItemRevisionResp afterResp, ChangeEntity changeEntity, ChangeAfterRLEntity changeAfterRLEntity) {
        Gte4RunTimeChangeAttachResp gte4Resp = (Gte4RunTimeChangeAttachResp)resp;
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        String relation = dictUtil.getRelation(changeAfterRLEntity.getObjectType(), EntityUtil.getObjectType(Gte4DistOrgEntryEntity.class));
        List<WorkspaceObjectEntity> gte4DistOrgEntryEntitys = masterRL.getChilds(changeAfterRLEntity.getUid(), relation, clazz -> {
                    return Gte4DistOrgEntryEntity.class.isAssignableFrom(clazz.getClass());
                });
        LocalDateTime effectiveDate = Optional.ofNullable(gte4DistOrgEntryEntitys).orElse(Collections.emptyList())
                .stream().map(Gte4DistOrgEntryEntity.class::cast)
                .map(Gte4DistOrgEntryEntity::getGte4EffectiveDate)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        gte4Resp.setGte4ChangeReason(changeAfterRLEntity.getChangeReason());
        gte4Resp.setGte4ChangeType(changeAfterRLEntity.getChangeType());
        Gte4PartRevisionResp partAfterResp = (Gte4PartRevisionResp)afterResp;
        gte4Resp.setPartNo(partAfterResp.getGte4PartNo());
        gte4Resp.setModelNo(partAfterResp.getGte4InitModel());
        gte4Resp.setEffictivedDate(Objects.isNull(effectiveDate) ? StrUtil.EMPTY :
                cn.hutool.core.date.DateUtil.format(effectiveDate, DatePattern.NORM_DATETIME_PATTERN));
    }
}
