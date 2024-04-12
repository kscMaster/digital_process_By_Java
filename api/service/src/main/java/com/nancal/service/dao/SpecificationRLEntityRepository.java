package com.nancal.service.dao;

import com.nancal.model.entity.SpecificationRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRLEntityRepository extends SoftDeleteRepository<SpecificationRLEntity, String>{}
