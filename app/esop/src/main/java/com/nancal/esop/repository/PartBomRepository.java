package com.nancal.esop.repository;

import com.nancal.esop.entity.PartBomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartBomRepository extends JpaRepository<PartBomEntity,PartBomEntity.PartBomPk> {
}
