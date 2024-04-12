package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4WorklineEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4WorklineEntityRepository extends SoftDeleteRepository<Gte4WorklineEntity, String>{}
