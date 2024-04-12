package com.nancal.service.dao;

import com.nancal.model.entity.EsopGaStationOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EsopGaStationOperationEntityRepository extends JpaRepository<EsopGaStationOperationEntity, Long>, QuerydslPredicateExecutor<EsopGaStationOperationEntity> {}
