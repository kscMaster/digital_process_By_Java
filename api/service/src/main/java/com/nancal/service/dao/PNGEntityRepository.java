package com.nancal.service.dao;

import com.nancal.model.entity.PNGEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PNGEntityRepository extends SoftDeleteRepository<PNGEntity, String>{}
