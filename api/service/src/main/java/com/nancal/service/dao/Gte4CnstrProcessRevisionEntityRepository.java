package com.nancal.service.dao;

import com.nancal.model.entity.Gte4CnstrProcessRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4CnstrProcessRevisionEntityRepository extends SoftDeleteRepository<Gte4CnstrProcessRevisionEntity, String>{}
