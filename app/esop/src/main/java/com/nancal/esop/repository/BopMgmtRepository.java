package com.nancal.esop.repository;

import com.nancal.esop.entity.BopMgmtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BopMgmtRepository extends JpaRepository<BopMgmtEntity,BopMgmtEntity.BopMgmtPk> {
}
