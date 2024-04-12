package com.nancal.service.dao;

import com.nancal.model.entity.Gte4PlantEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PlantEntityRepository extends SoftDeleteRepository<Gte4PlantEntity, String>{}
