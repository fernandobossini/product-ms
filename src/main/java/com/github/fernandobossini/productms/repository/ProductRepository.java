package com.github.fernandobossini.productms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.product.ProductRepositoryQuery;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {

}
