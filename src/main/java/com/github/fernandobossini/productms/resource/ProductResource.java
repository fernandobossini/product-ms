package com.github.fernandobossini.productms.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.fernandobossini.productms.exception.ApiError;
import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;
import com.github.fernandobossini.productms.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Product product, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(new ApiError(result, HttpStatus.BAD_REQUEST));
		}

		Product productSave = productService.save(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(productSave);
	}

	@GetMapping("/{id}")	  
	public Product findById(@PathVariable Long id) {
		return productService
				.findById(id)			
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
		Product product = productService
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		productService.delete(product);
	}


}
