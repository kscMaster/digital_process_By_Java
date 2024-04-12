package com.nancal.service.dao;

import com.nancal.model.entity.TableRowEntity;
import com.nancal.service.dao.base.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRowEntityRepository extends SoftDeleteRepository<TableRowEntity, String>{}
