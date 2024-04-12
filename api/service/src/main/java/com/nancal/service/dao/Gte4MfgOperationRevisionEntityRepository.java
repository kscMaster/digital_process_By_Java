package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MfgOperationRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgOperationRevisionEntityRepository extends SoftDeleteRepository<Gte4MfgOperationRevisionEntity, String>{}
