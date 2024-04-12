package com.nancal.service.dao;

import com.nancal.model.entity.Gte4InspectionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4InspectionEntityRepository extends SoftDeleteRepository<Gte4InspectionEntity, String>{}
