package com.nancal.service.dao;

import com.nancal.model.entity.EquipmentEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentEntityRepository extends SoftDeleteRepository<EquipmentEntity, String>{}
