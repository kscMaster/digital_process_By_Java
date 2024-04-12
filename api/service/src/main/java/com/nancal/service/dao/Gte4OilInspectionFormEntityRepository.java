package com.nancal.service.dao;

import com.nancal.model.entity.Gte4OilInspectionFormEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4OilInspectionFormEntityRepository extends SoftDeleteRepository<Gte4OilInspectionFormEntity, String>{}
