package com.big.store.nebuchadnezzar.repository;

import com.big.store.nebuchadnezzar.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
