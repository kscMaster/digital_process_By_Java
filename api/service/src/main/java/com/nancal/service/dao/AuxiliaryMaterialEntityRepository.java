package com.nancal.service.dao;

import com.nancal.model.entity.AuxiliaryMaterialEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuxiliaryMaterialEntityRepository extends SoftDeleteRepository<AuxiliaryMaterialEntity, String>{}
