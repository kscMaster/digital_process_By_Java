package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TechDocPdmEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TechDocPdmEntityRepository extends SoftDeleteRepository<Gte4TechDocPdmEntity, String>{}
