package com.nancal.service.dao;

import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRevisionEntityRepository extends SoftDeleteRepository<ItemRevisionEntity, String>{}
