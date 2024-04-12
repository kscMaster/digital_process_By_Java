package com.nancal.service.dao;

import com.nancal.model.entity.MasterRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterRLEntityRepository extends SoftDeleteRepository<MasterRLEntity, String>{}
