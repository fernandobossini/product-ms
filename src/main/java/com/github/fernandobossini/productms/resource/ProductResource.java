package com.github.fernandobossini.productms.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.fernandobossini.productms.exceptionhandler.ApiExceptionHandler.Error;
import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;
import com.github.fernandobossini.productms.service.ProductService;
import com.github.fernandobossini.productms.service.exception.ProductNotFound;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Product product) {
		Product productSave = productService.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(productSave);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Product product) {
		try {
			Product productUpdate = productService.update(id, product);
			return ResponseEntity.ok(productUpdate);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}")	  
	public Product findById(@PathVariable Long id) {
		return productService.findById(id);				
	}

	@GetMapping
	public List<Product> list() {
		return productService.findAll();
	}

	@GetMapping("/search")	
	public List<Product> search(ProductFilter productFilter) {
		return productService.search(productFilter);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {				
		productService.delete(id);
	}

	@ExceptionHandler(ProductNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleProductNotFound(ProductNotFound exception) {
		Error error = new Error(HttpStatus.NOT_FOUND.value(), exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

}
