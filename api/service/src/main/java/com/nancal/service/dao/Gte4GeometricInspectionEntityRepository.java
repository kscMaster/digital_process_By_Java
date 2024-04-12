package com.nancal.service.dao;

import com.nancal.model.entity.Gte4GeometricInspectionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4GeometricInspectionEntityRepository extends SoftDeleteRepository<Gte4GeometricInspectionEntity, String>{}
