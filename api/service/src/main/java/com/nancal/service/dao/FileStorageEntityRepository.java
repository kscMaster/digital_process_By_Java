package com.nancal.service.dao;

import com.nancal.model.entity.FileStorageEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageEntityRepository extends SoftDeleteRepository<FileStorageEntity, String>{}
