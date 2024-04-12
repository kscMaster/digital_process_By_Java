package com.nancal.service.dao;

import com.nancal.model.entity.MeasureEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureEntityRepository extends SoftDeleteRepository<MeasureEntity, String>{}
