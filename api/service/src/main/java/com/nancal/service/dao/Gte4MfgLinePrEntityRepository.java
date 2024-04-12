package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4MfgLinePrEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgLinePrEntityRepository extends SoftDeleteRepository<Gte4MfgLinePrEntity, String>{}
