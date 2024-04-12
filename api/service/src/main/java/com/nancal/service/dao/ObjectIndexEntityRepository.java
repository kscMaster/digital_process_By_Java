package com.nancal.service.dao;

import com.nancal.model.entity.ObjectIndexEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectIndexEntityRepository extends SoftDeleteRepository<ObjectIndexEntity, String>{}
