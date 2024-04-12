package com.nancal.service.dao;

import com.nancal.model.entity.Gte4MaterialRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4MaterialRevisionEntityRepository extends SoftDeleteRepository<Gte4MaterialRevisionEntity, String>{}
