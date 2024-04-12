package com.nancal.service.dao;

import com.nancal.model.entity.GIFEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GIFEntityRepository extends SoftDeleteRepository<GIFEntity, String>{}
