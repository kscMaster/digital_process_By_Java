package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgOperationEntityRepository extends SoftDeleteRepository<Gte4MfgOperationEntity, String>{}
