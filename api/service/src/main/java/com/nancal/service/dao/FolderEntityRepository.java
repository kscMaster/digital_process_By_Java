package com.nancal.service.dao;

import com.nancal.model.entity.FolderEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderEntityRepository extends SoftDeleteRepository<FolderEntity, String>{}
