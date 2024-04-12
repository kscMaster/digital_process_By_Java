package com.nancal.service.dao;

import com.nancal.model.entity.Gte4CleanupMachiningEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4CleanupMachiningEntityRepository extends SoftDeleteRepository<Gte4CleanupMachiningEntity, String>{}
