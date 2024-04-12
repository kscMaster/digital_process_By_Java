package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TechDocumentRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TechDocumentRevisionEntityRepository extends SoftDeleteRepository<Gte4TechDocumentRevisionEntity, String>{}
