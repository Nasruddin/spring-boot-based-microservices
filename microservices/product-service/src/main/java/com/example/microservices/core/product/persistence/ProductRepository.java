package com.example.microservices.core.product.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String>, CrudRepository<ProductEntity, String> {
    Optional<ProductEntity> findByProductId(int productId);
}
