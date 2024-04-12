package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4PlantRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PlantRevisionEntityRepository extends SoftDeleteRepository<Gte4PlantRevisionEntity, String>{}
