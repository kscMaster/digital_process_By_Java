package com.nancal.service.dao;

import com.nancal.model.entity.ChangeEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeEntityRepository extends SoftDeleteRepository<ChangeEntity, String>{}
