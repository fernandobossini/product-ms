package com.github.fernandobossini.productms.repository.filter;

import java.math.BigDecimal;

public class ProductFilter {
	
	private String q;
	
	private BigDecimal minPrice;
	
	private BigDecimal maxPrice;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setmin_price(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setmax_price(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}	

}
