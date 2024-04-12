package com.nancal.esop.repository;

import com.nancal.esop.entity.PppRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PppRelationRepository extends JpaRepository<PppRelationEntity,String> {
}
