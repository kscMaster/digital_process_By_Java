package com.nancal.service.dao;

import com.nancal.model.entity.Gte4CnstrProcessEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4CnstrProcessEntityRepository extends SoftDeleteRepository<Gte4CnstrProcessEntity, String>{}
