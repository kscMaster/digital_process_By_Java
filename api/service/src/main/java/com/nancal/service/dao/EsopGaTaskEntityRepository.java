package com.nancal.service.dao;

import com.nancal.model.entity.EsopGaTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EsopGaTaskEntityRepository extends JpaRepository<EsopGaTaskEntity, String>, QuerydslPredicateExecutor<EsopGaTaskEntity> {}
