package com.nancal.service.dao;

import com.nancal.model.entity.Gte4ClientageRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4ClientageRLEntityRepository extends SoftDeleteRepository<Gte4ClientageRLEntity, String>{}
