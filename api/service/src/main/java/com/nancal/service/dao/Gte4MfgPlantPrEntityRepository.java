package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4MfgPlantPrEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgPlantPrEntityRepository extends SoftDeleteRepository<Gte4MfgPlantPrEntity, String>{}
