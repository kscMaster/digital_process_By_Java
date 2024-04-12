package com.nancal.service.dao;

import com.nancal.model.entity.AttachRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRLEntityRepository extends SoftDeleteRepository<AttachRLEntity, String>{}
