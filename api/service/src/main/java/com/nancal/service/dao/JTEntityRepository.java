package com.nancal.service.dao;

import com.nancal.model.entity.JTEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JTEntityRepository extends SoftDeleteRepository<JTEntity, String>{}
