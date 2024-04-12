package com.nancal.service.dao;

import com.nancal.model.entity.DocumentRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRevisionEntityRepository extends SoftDeleteRepository<DocumentRevisionEntity, String>{}
