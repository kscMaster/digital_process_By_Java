package com.nancal.service.dao;

import com.nancal.model.entity.Gte4InspectionRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4InspectionRevisionEntityRepository extends SoftDeleteRepository<Gte4InspectionRevisionEntity, String>{}
