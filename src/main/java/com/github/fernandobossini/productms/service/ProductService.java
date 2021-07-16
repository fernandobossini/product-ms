package com.github.fernandobossini.productms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.ProductRepository;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);		
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public void delete(Product product) {
		productRepository.delete(product);
	}

	public List<Product> search(ProductFilter productFilter) {
		return productRepository.search(productFilter);
	}


}
