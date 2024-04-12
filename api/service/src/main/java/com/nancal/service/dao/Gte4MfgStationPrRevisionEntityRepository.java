package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4MfgStationPrRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgStationPrRevisionEntityRepository extends SoftDeleteRepository<Gte4MfgStationPrRevisionEntity, String>{}
