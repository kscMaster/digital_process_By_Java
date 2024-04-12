package com.nancal.service.service;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.MfgTargetRLEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.IncludeRLFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.MfgTargetRLFactory;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IGte4MfgProcessRevisionDomainService extends IMfgProcessRevisionDomainService{
}
