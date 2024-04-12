package com.nancal.service.dao;

import com.nancal.model.entity.Gte4CleanupMachiningRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4CleanupMachiningRLEntityRepository extends SoftDeleteRepository<Gte4CleanupMachiningRLEntity, String>{}
