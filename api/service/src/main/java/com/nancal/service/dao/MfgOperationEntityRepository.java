package com.nancal.service.dao;

import com.nancal.model.entity.MfgOperationEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgOperationEntityRepository extends SoftDeleteRepository<MfgOperationEntity, String>{}
