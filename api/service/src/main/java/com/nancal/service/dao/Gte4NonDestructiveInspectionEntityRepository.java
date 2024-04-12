package com.nancal.service.dao;

import com.nancal.model.entity.Gte4NonDestructiveInspectionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4NonDestructiveInspectionEntityRepository extends SoftDeleteRepository<Gte4NonDestructiveInspectionEntity, String>{}
