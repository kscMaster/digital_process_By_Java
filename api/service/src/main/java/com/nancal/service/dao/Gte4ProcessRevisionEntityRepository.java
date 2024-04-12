package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.Gte4ProcessRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4ProcessRevisionEntityRepository extends SoftDeleteRepository<Gte4ProcessRevisionEntity, String>{}
