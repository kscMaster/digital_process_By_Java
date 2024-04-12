package com.nancal.esop.repository;

import com.nancal.esop.entity.OperationRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRelationRepository extends JpaRepository<OperationRelationEntity,OperationRelationEntity.OperationRelationPk> {
}
