package com.nancal.service.dao;

import com.nancal.model.entity.Gte4PartToolRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4PartToolRevisionEntityRepository extends SoftDeleteRepository<Gte4PartToolRevisionEntity, String>{}
