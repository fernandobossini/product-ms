package com.github.fernandobossini.productms.repository.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.github.fernandobossini.productms.model.Product;
import com.github.fernandobossini.productms.repository.filter.ProductFilter;

public class ProductRepositoryImpl implements ProductRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Product> search(ProductFilter productFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
		Root<Product> root = criteria.from(Product.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(productFilter.getQ())) {
			Predicate predicateForName = builder.like(builder.lower(root.get("name")), "%" + productFilter.getQ().toLowerCase() + "%");
			Predicate predicateForDescription = builder.like(builder.lower(root.get("description")), "%" + productFilter.getQ().toLowerCase() + "%");
			predicates.add(builder.or(predicateForName, predicateForDescription));
		}
		
		if (productFilter.getMinPrice() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("price").as(BigDecimal.class), productFilter.getMinPrice()));
		}
		
		if (productFilter.getMaxPrice() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("price").as(BigDecimal.class), productFilter.getMaxPrice()));
		}
				
		criteria.where(predicates.toArray(new Predicate[predicates.size()]));		
		TypedQuery<Product> query = manager.createQuery(criteria);		
		return query.getResultList();
	}	
}
