package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4PlantRLEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PlantRLEntityRepository extends SoftDeleteRepository<Gte4PlantRLEntity, String>{}
