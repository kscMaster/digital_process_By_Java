package com.nancal.service.dao;

import com.nancal.model.entity.WPSExcelEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WPSExcelEntityRepository extends SoftDeleteRepository<WPSExcelEntity, String>{}
