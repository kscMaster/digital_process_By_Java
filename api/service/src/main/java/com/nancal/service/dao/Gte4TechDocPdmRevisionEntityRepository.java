package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TechDocPdmRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TechDocPdmRevisionEntityRepository extends SoftDeleteRepository<Gte4TechDocPdmRevisionEntity, String>{}
