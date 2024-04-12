package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4WorklineRLEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4WorklineRLEntityRepository extends SoftDeleteRepository<Gte4WorklineRLEntity, String>{}
