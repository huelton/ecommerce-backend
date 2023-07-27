package com.commerce.dscatalog.tests;

import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.projections.ProductProjection;

public class TestProductProjection implements ProductProjection {

	private Long id;
	private String name;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void insertData(Product product) {
		this.id = product.getId();
		this.name = product.getName();
	}
}