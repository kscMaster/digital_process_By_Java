package com.nancal.service.dao;

import com.nancal.model.entity.MfgProcessRouteEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgProcessRouteEntityRepository extends SoftDeleteRepository<MfgProcessRouteEntity, String>{}
