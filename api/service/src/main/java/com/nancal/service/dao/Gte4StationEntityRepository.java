package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4StationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4StationEntityRepository extends SoftDeleteRepository<Gte4StationEntity, String>{}
