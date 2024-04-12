package com.nancal.service.dao;

import com.nancal.model.entity.MfgStepEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgStepEntityRepository extends SoftDeleteRepository<MfgStepEntity, String>{}
