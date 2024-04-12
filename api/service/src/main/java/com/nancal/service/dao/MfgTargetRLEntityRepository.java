package com.nancal.service.dao;

import com.nancal.model.entity.MfgTargetRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgTargetRLEntityRepository extends SoftDeleteRepository<MfgTargetRLEntity, String>{}
