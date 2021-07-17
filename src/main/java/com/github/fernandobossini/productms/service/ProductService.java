package com.github.fernandobossini.productms.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.ProductRepository;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;
import com.github.fernandobossini.productms.service.exception.ProductNotFound;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFound("Produto não encontrado"));
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public void delete(Long id) {
		Product product = findById(id);
		productRepository.delete(product);
	}

	public List<Product> search(ProductFilter productFilter) {
		return productRepository.search(productFilter);
	}

	public Product update(Long id, Product product) {
		Product productSaved = findById(id);		
		BeanUtils.copyProperties(product, productSaved, "id");
		return productRepository.save(productSaved);
	}


}
