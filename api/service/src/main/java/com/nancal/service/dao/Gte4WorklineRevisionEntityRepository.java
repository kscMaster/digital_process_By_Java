package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4WorklineRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4WorklineRevisionEntityRepository extends SoftDeleteRepository<Gte4WorklineRevisionEntity, String>{}
