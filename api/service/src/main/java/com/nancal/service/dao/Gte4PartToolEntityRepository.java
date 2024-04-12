package com.nancal.service.dao;

import com.nancal.model.entity.Gte4PartToolEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PartToolEntityRepository extends SoftDeleteRepository<Gte4PartToolEntity, String>{}
