package com.nancal.esop.repository;

import com.nancal.esop.entity.PartMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartMaterialRepository extends JpaRepository<PartMaterialEntity,PartMaterialEntity.PartMaterialPk> {
}
