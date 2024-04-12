package com.nancal.service.dao;

import com.nancal.model.entity.Gte4StationRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4StationRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4StationRevisionEntityRepository extends SoftDeleteRepository<Gte4StationRevisionEntity, String> {}
