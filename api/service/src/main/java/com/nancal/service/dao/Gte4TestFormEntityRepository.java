package com.nancal.service.dao;

import com.nancal.model.entity.Gte4TestFormEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4TestFormEntityRepository extends SoftDeleteRepository<Gte4TestFormEntity, String>{}
