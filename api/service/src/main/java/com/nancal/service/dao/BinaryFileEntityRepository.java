package com.nancal.service.dao;

import com.nancal.model.entity.BinaryFileEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryFileEntityRepository extends SoftDeleteRepository<BinaryFileEntity, String>{}
