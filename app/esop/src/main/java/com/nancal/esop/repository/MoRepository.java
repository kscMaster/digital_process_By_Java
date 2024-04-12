package com.nancal.esop.repository;

import com.nancal.esop.entity.MoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoRepository extends JpaRepository<MoEntity,String> {
}
