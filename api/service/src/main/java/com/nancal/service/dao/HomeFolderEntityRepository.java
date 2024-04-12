package com.nancal.service.dao;

import com.nancal.model.entity.HomeFolderEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeFolderEntityRepository extends SoftDeleteRepository<HomeFolderEntity, String>{}
