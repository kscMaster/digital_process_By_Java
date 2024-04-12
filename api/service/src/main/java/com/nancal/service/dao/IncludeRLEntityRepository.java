package com.nancal.service.dao;

import com.nancal.model.entity.IncludeRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncludeRLEntityRepository extends SoftDeleteRepository<IncludeRLEntity, String>{}
