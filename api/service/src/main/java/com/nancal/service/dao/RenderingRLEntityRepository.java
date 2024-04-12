package com.nancal.service.dao;

import com.nancal.model.entity.RenderingRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenderingRLEntityRepository extends SoftDeleteRepository<RenderingRLEntity, String>{}
