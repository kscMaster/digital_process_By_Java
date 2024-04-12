package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4ProcessEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4ProcessEntityRepository extends SoftDeleteRepository<Gte4ProcessEntity, String>{}
