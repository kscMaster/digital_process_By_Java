package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4MfgLinePrRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgLinePrRevisionEntityRepository extends SoftDeleteRepository<Gte4MfgLinePrRevisionEntity, String>{}
