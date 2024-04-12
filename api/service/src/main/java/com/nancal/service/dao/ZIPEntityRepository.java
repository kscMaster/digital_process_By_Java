package com.nancal.service.dao;

import com.nancal.model.entity.ZIPEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZIPEntityRepository extends SoftDeleteRepository<ZIPEntity, String>{}
