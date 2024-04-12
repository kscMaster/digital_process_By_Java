package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MfgProcessRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgProcessRevisionEntityRepository extends SoftDeleteRepository<Gte4MfgProcessRevisionEntity, String>{}
