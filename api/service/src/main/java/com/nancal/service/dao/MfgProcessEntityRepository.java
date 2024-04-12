package com.nancal.service.dao;

import com.nancal.model.entity.MfgProcessEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgProcessEntityRepository extends SoftDeleteRepository<MfgProcessEntity, String>{}
