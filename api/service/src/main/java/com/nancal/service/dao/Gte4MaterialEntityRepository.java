package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MaterialEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MaterialEntityRepository extends SoftDeleteRepository<Gte4MaterialEntity, String>{}
