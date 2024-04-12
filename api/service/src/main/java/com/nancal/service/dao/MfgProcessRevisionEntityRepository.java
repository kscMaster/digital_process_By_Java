package com.nancal.service.dao;

import com.nancal.model.entity.MfgProcessRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgProcessRevisionEntityRepository extends SoftDeleteRepository<MfgProcessRevisionEntity, String>{}
