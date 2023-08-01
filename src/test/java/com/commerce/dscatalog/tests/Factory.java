package com.commerce.dscatalog.tests;

import java.time.Instant;

import org.springframework.mail.SimpleMailMessage;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.entities.Category;
import com.commerce.dscatalog.entities.City;
import com.commerce.dscatalog.entities.DeliveryAddress;
import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.OrderItem;
import com.commerce.dscatalog.entities.OrderItemPK;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.State;
import com.commerce.dscatalog.entities.Status;
import com.commerce.dscatalog.entities.User;

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
		TestProductProjection testProjection = new TestProductProjection();
		testProjection.insertData(product);
		return testProjection;
	}

	public static User createUser() {
		User user = new User(1L, "NameUser", "LastNameUser", "teste@gmail.com", "12345");
		return user;
	}

	public static State createState() {
		State state = new State(1L, "NameState");
		return state;
	}

	public static City createCity() {
		City city = new City(1L, "NameCity", createState());
		return city;
	}

	public static DeliveryAddress createAddress() {
		DeliveryAddress address = new DeliveryAddress(1L, "Test Street", "111", "Apartment 11", "Village", "07890-230",
				true, createUser(), createCity());
		return address;
	}

	public static Status createStatus() {
		Status status = new Status(1L, "ABERTO");
		return status;
	}

	public static Order createOrder() {
		Order order = new Order(1L, Instant.parse("2023-05-20T03:00:00Z"), Instant.parse("2023-05-20T04:00:00Z"),
				createUser(), createAddress(), createStatus());

		return order;
	}

	public static OrderItem createOrderItem() {
		OrderItemPK itemPK = new OrderItemPK(createOrder(), createProduct());
		OrderItem orderItem = new OrderItem(itemPK.getOrder(), itemPK.getProduct(), 2.0, 2, 120.0);
		return orderItem;
	}

	public static SimpleMailMessage createSimpleMailMessage() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(null);
		simpleMailMessage.setReplyTo(null);
		simpleMailMessage.setTo("teste@gmail.com");
		simpleMailMessage.setSentDate(null);
		simpleMailMessage.setSubject("Pedido Confirmado! Código: 1");
		simpleMailMessage.setText("Order Number: 1, create date: 20/05/2023 03:00:00, user: teste@gmail.com\r\n"
				+ "Details:\r\n" + "Total Value Order: R$ 0,00");

		return simpleMailMessage;
	}

}
