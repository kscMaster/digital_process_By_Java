package com.nancal.service.dao;

import com.nancal.model.entity.JigRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JigRevisionEntityRepository extends SoftDeleteRepository<JigRevisionEntity, String>{}
