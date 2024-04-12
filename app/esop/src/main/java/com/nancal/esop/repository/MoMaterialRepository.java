package com.nancal.esop.repository;

import com.nancal.esop.entity.MoMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoMaterialRepository extends JpaRepository<MoMaterialEntity,MoMaterialEntity.MoMaterialPk> {
}
