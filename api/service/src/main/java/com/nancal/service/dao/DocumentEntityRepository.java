package com.nancal.service.dao;

import com.nancal.model.entity.DocumentEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentEntityRepository extends SoftDeleteRepository<DocumentEntity, String>{}
