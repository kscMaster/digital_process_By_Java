package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TestRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TestRLEntityRepository extends SoftDeleteRepository<Gte4TestRLEntity, String>{}
