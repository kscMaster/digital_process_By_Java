package com.nancal.service.dao;

import com.nancal.model.entity.MfgProcessRouteRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MfgProcessRouteRevisionEntityRepository extends SoftDeleteRepository<MfgProcessRouteRevisionEntity, String>{}
