package com.nancal.service.bo;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.model.entity.QSpecificationRLEntity;
import com.nancal.model.entity.SpecificationRLEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "SpecificationRL 规范 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SpecificationRL extends Relation  implements Serializable{

    @Override
    public String getRelationType() {
        return "SpecificationRL";
    }

    public List<WorkspaceObjectEntity> getRightObjectList(List<String> uids,String leftObjectType){
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        BooleanBuilder builder = new BooleanBuilder();
        QSpecificationRLEntity specificationRL = QSpecificationRLEntity.specificationRLEntity;
        builder.and(specificationRL.delFlag.isFalse());
        builder.and(specificationRL.leftObjectType.eq(leftObjectType));
        builder.and(specificationRL.leftObject.in(uids));
        List<SpecificationRLEntity> entityList = jpaQueryFactory.selectFrom(specificationRL).where(builder).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<WorkspaceObjectEntity> list = new ArrayList<>();
        entityList.stream().collect(Collectors.groupingBy(SpecificationRLEntity::getRightObjectType)).
                forEach((objectType,values)->{
                    List<WorkspaceObjectEntity> result = EntityUtil.getByIds(objectType,  values.stream().map(SpecificationRLEntity::getRightObject).collect(Collectors.toList()));
                    list.addAll(Optional.ofNullable(result).orElse(new ArrayList<>()));
                });
        return list;
    }


}