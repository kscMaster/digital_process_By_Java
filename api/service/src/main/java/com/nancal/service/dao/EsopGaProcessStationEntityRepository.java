package com.nancal.service.dao;

import com.nancal.model.entity.EsopGaProcessStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EsopGaProcessStationEntityRepository extends JpaRepository<EsopGaProcessStationEntity, Long>, QuerydslPredicateExecutor<EsopGaProcessStationEntity> {}
