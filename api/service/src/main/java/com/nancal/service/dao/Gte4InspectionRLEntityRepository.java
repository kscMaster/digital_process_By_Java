package com.nancal.service.dao;

import com.nancal.model.entity.Gte4InspectionRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4InspectionRLEntityRepository extends SoftDeleteRepository<Gte4InspectionRLEntity, String>{}
