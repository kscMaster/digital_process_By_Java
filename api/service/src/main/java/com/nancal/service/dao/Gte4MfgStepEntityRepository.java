package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MfgStepEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgStepEntityRepository extends SoftDeleteRepository<Gte4MfgStepEntity, String>{}
