package com.nancal.service.dao;

import com.nancal.model.entity.LibraryIncludeRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryIncludeRLEntityRepository extends SoftDeleteRepository<LibraryIncludeRLEntity, String>{}
