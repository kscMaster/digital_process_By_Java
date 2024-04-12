package com.nancal.esop.repository;

import com.nancal.esop.entity.DesignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignRepository extends JpaRepository<DesignEntity,String> {
}
