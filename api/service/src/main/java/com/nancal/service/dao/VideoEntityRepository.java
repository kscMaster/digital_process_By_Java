package com.nancal.service.dao;

import com.nancal.service.dao.base.SoftDeleteRepository;
import com.nancal.model.entity.VideoEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoEntityRepository extends SoftDeleteRepository<VideoEntity, String>{}
