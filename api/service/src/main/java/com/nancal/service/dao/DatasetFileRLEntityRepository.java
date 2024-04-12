package com.nancal.service.dao;

import com.nancal.model.entity.DatasetFileRLEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetFileRLEntityRepository extends SoftDeleteRepository<DatasetFileRLEntity, String> {
    List<DatasetFileRLEntity> findByLeftObject(String id);
}
