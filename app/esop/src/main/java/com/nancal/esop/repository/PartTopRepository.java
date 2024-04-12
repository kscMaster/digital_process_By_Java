package com.nancal.esop.repository;

import com.nancal.esop.entity.PartTopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartTopRepository extends JpaRepository<PartTopEntity,String> {
}
