package com.nancal.esop.repository;

import com.nancal.esop.entity.ProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, ProcessEntity.ProcessPk> {
}
