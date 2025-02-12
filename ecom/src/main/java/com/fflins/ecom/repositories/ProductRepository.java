package com.fflins.ecom.repositories;

import com.fflins.ecom.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
List<Product> findByCategory(String category);
List<Product> findByNameContaining(String name);
}