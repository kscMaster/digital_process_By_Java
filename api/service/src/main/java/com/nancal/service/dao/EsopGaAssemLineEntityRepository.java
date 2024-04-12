package com.nancal.service.dao;

import com.nancal.model.entity.EsopGaAssemLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EsopGaAssemLineEntityRepository extends JpaRepository<EsopGaAssemLineEntity, String>, QuerydslPredicateExecutor<EsopGaAssemLineEntity> {}
