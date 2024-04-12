package com.nancal.service.dao;

import com.nancal.model.entity.JPEGEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPEGEntityRepository extends SoftDeleteRepository<JPEGEntity, String>{}
