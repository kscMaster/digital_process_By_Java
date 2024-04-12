package com.nancal.service.dao;

import com.nancal.model.entity.Gte4EntrustRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4EntrustRLEntityRepository extends SoftDeleteRepository<Gte4EntrustRLEntity, String>{}
