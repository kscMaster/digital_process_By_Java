package com.nancal.service.dao;

import com.nancal.model.entity.JigEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JigEntityRepository extends SoftDeleteRepository<JigEntity, String>{}
