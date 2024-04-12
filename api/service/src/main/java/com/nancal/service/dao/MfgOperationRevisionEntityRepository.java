package com.nancal.service.dao;

import com.nancal.model.entity.MfgOperationRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgOperationRevisionEntityRepository extends SoftDeleteRepository<MfgOperationRevisionEntity, String>{}
