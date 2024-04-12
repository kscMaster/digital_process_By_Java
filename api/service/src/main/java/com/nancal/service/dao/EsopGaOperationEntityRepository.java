package com.nancal.service.dao;

import com.nancal.model.entity.EsopGaOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EsopGaOperationEntityRepository extends JpaRepository<EsopGaOperationEntity, String>, QuerydslPredicateExecutor<EsopGaOperationEntity> {}
