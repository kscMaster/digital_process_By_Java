package com.nancal.service.dao;

import com.nancal.model.entity.BOMViewEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BOMViewEntityRepository extends SoftDeleteRepository<BOMViewEntity, String>{}
