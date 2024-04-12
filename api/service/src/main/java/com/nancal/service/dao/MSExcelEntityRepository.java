package com.nancal.service.dao;

import com.nancal.model.entity.MSExcelEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MSExcelEntityRepository extends SoftDeleteRepository<MSExcelEntity, String>{}
