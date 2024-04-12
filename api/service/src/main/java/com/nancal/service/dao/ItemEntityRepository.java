package com.nancal.service.dao;

import com.nancal.model.entity.ItemEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemEntityRepository extends SoftDeleteRepository<ItemEntity, String>{}
