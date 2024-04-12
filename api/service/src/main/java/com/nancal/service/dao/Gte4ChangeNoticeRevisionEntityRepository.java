package com.nancal.service.dao;

import com.nancal.model.entity.Gte4ChangeNoticeRevisionEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gte4ChangeNoticeRevisionEntityRepository extends SoftDeleteRepository<Gte4ChangeNoticeRevisionEntity, String>{}
