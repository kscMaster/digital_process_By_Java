package com.nancal.service.dao;

import com.nancal.model.entity.ToolEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolEntityRepository extends SoftDeleteRepository<ToolEntity, String>{}
