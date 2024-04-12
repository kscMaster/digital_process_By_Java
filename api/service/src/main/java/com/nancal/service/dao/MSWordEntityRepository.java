package com.nancal.service.dao;

import com.nancal.model.entity.MSWordEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MSWordEntityRepository extends SoftDeleteRepository<MSWordEntity, String>{}
