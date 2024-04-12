package com.nancal.service.dao;

import com.nancal.model.entity.ToolRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRevisionEntityRepository extends SoftDeleteRepository<ToolRevisionEntity, String>{}
