package com.nancal.service.dao;

import com.nancal.model.entity.PDFEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDFEntityRepository extends SoftDeleteRepository<PDFEntity, String>{}
