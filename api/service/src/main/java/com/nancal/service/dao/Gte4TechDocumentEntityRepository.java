package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TechDocumentEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TechDocumentEntityRepository extends SoftDeleteRepository<Gte4TechDocumentEntity, String>{}
