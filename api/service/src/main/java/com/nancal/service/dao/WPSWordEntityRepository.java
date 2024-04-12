package com.nancal.service.dao;

import com.nancal.model.entity.WPSWordEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WPSWordEntityRepository extends SoftDeleteRepository<WPSWordEntity, String>{}
