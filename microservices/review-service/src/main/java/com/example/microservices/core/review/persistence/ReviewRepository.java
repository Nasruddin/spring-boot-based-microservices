package com.example.microservices.core.review.persistence;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Integer> {

    @Transactional(readOnly = true)
    List<ReviewEntity> findByProductId(int productId);
}