package com.nancal.service.dao;

import com.nancal.model.entity.ChangeAfterRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeAfterRLEntityRepository extends SoftDeleteRepository<ChangeAfterRLEntity, String>{}
