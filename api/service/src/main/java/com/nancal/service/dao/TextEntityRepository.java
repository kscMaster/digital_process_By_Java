package com.nancal.service.dao;

import com.nancal.model.entity.TextEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextEntityRepository extends SoftDeleteRepository<TextEntity, String>{}
