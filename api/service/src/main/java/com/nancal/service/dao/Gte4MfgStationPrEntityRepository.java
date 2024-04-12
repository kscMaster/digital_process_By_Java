package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4MfgStationPrEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgStationPrEntityRepository extends SoftDeleteRepository<Gte4MfgStationPrEntity, String>{}
