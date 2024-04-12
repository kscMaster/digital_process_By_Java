package com.nancal.service.dao;

import com.nancal.model.entity.ChangeBeforeRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeBeforeRLEntityRepository extends SoftDeleteRepository<ChangeBeforeRLEntity, String>{}
