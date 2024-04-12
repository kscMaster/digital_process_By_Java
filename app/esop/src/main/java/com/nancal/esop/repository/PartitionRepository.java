package com.nancal.esop.repository;

import com.nancal.esop.entity.PartitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartitionRepository extends JpaRepository<PartitionEntity, PartitionEntity.PartitionPk> {
}
