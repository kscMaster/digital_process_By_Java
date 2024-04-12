package com.nancal.service.dao;

import com.nancal.model.entity.Gte4ChangeNoticeEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4ChangeNoticeEntityRepository extends SoftDeleteRepository<Gte4ChangeNoticeEntity, String>{}
