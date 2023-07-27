package com.commerce.dscatalog.tests;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.entities.Category;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.projections.ProductProjection;

import java.time.Instant;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png",
				Instant.parse("2023-05-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		Category category = new Category(1L, "Livros");
		return category;
	}
	
	public static Category createCategoryToUpdate() {
		Category category = createCategory();
		category.setName("Update Livro");
		return category;
	}

	public static CategoryDTO createCategoryDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}
	
	public static CategoryDTO createCategoryDTOToInsert() {
		Category category = new Category(null, "Livros");
		return new CategoryDTO(category);
	}
	
	public static CategoryDTO createCategoryDTOToUpdate() {
		Category category = createCategoryToUpdate();
		return new CategoryDTO(category);
	}
	

	public static TestProductProjection createProjection(Product product) {
		// Crie uma instância de TestProductProjection
		TestProductProjection testProjection = new TestProductProjection();
		// Insira os dados do produto na projeção de teste
		testProjection.insertData(product);

		return testProjection;

	}
}
