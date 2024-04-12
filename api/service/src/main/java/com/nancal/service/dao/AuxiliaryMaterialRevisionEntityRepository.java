package com.nancal.service.dao;

import com.nancal.model.entity.AuxiliaryMaterialRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuxiliaryMaterialRevisionEntityRepository extends SoftDeleteRepository<AuxiliaryMaterialRevisionEntity, String>{}
