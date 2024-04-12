package com.nancal.service.dao;

import com.nancal.model.entity.EquipmentRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRevisionEntityRepository extends SoftDeleteRepository<EquipmentRevisionEntity, String>{}
