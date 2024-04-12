package com.nancal.esop.repository;

import com.nancal.esop.entity.LineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends JpaRepository<LineEntity,LineEntity.LinePk> {
}
