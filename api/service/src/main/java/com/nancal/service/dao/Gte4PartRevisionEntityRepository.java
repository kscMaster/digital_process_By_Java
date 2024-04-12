package com.nancal.service.dao;

import com.nancal.model.entity.Gte4PartRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PartRevisionEntityRepository extends SoftDeleteRepository<Gte4PartRevisionEntity, String>{}
