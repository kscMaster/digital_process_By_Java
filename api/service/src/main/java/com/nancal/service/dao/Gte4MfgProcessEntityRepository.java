package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MfgProcessEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MfgProcessEntityRepository extends SoftDeleteRepository<Gte4MfgProcessEntity, String>{}
