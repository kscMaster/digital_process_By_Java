package com.nancal.service.dao;

import com.nancal.model.entity.MfgStepRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgStepRLEntityRepository extends SoftDeleteRepository<MfgStepRLEntity, String>{}
