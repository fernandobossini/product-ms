package com.github.fernandobossini.productms.repository.product;

import java.util.List;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;

public interface ProductRepositoryQuery {
	
	public List<Product> search(ProductFilter productFilter);
	
}
