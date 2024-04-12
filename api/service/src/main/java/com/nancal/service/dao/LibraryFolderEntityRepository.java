package com.nancal.service.dao;

import com.nancal.model.entity.LibraryFolderEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryFolderEntityRepository extends SoftDeleteRepository<LibraryFolderEntity, String>{}
