package com.nancal.esop.repository;

import com.nancal.esop.entity.ProductBopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBopRepository extends JpaRepository<ProductBopEntity, String> {
}
