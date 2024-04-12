package com.nancal.service.dao;

import com.nancal.model.entity.DWGEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DWGEntityRepository extends SoftDeleteRepository<DWGEntity, String>{}
