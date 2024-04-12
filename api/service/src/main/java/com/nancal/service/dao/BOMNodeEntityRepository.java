package com.nancal.service.dao;

import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BOMNodeEntityRepository extends SoftDeleteRepository<BOMNodeEntity, String>{
    List<BOMNodeEntity> findByChildItem(String childUid);
    List<BOMNodeEntity> findByParentItem(String parentUid);
}
