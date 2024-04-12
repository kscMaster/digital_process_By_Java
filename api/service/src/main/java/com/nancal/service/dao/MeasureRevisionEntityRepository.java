package com.nancal.service.dao;

import com.nancal.model.entity.MeasureRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRevisionEntityRepository extends SoftDeleteRepository<MeasureRevisionEntity, String>{}
