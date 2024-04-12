package com.nancal.service.dao;

import com.nancal.model.entity.MSPowerPointEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MSPowerPointEntityRepository extends SoftDeleteRepository<MSPowerPointEntity, String>{}
