package com.nancal.service.dao;

import com.nancal.model.entity.Gte4PartEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PartEntityRepository extends SoftDeleteRepository<Gte4PartEntity, String>{}
