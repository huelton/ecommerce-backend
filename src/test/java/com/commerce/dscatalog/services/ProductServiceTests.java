package com.commerce.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.commerce.dscatalog.tests.Constants.PAGE;
import static com.commerce.dscatalog.tests.Constants.SIZE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.projections.ProductProjection;
import com.commerce.dscatalog.repositories.CategoryRepository;
import com.commerce.dscatalog.repositories.ProductRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;

import com.commerce.dscatalog.tests.Factory;


import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private CategoryRepository categoryRepository;

	private PageImpl<Product> page;
	private long existingId;
	private long notExistingId;
	private long dependentId;
	private List<Long> categoryIds;
	private List<Long> productIds;
	private Product product, productSaved, productUpdate;
	private ProductProjection productProjection;
	private PageImpl<ProductProjection> pageProductProjection;

	@BeforeEach
	void setup() {
		existingId = 1L;
		notExistingId = 1000L;
		dependentId = 4;
		product = Factory.createProduct();
		productSaved = Factory.createProduct();
		productUpdate = Factory.createProduct();
		productProjection = Factory.createProjection(product);
		categoryIds = List.of(1L, 2L);
		productIds = List.of(1L);
		page = new PageImpl<>(List.of(product));
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		pageProductProjection = new PageImpl<>(List.of(productProjection));

		when(productRepository.findAll(pageable)).thenReturn(page);

		when(productRepository.searchProducts(categoryIds, "Playstation", pageable)).thenReturn(pageProductProjection);
		when(productRepository.searchProductsWithCategories(productIds)).thenReturn(List.of(product, product, product));
		when(productRepository.save(product)).thenReturn(productSaved);
		when(productRepository.save(product)).thenReturn(productUpdate);
		when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		when(productRepository.findById(notExistingId)).thenReturn(Optional.empty());

		doThrow(DatabaseException.class).when(productRepository).deleteById(dependentId);

		when(productRepository.getReferenceById(existingId)).thenReturn(product);

		when(productRepository.existsById(existingId)).thenReturn(true);
		when(productRepository.existsById(notExistingId)).thenReturn(false);
		when(productRepository.existsById(dependentId)).thenReturn(true);

	}

	@Test
	public void insertProductShouldReturnProductDTO() throws Exception {

		ProductDTO inputDto = new ProductDTO();
		inputDto.setName("Test Product");
		inputDto.setDescription("Test Description");

		Product savedProduct = new Product();
		savedProduct.setId(1L);

		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
		productService.acessoAoMetodo(inputDto, savedProduct);
		ProductDTO resultDto = productService.insert(inputDto);
		verify(productRepository, times(1)).save(any(Product.class));

		assertNotNull(resultDto);
		assertEquals(savedProduct.getId(), resultDto.getId());
		assertEquals(inputDto.getName(), resultDto.getName());
		assertEquals(inputDto.getDescription(), resultDto.getDescription());
	}

	@Test
	public void updateProductShouldReturnProductDTO() {

		Long id = 1L;
		ProductDTO inputDto = new ProductDTO();
		inputDto.setName("Updated Product");
		inputDto.setDescription("Updated Description");
		Product existingProduct = new Product();
		existingProduct.setId(id);

		when(productRepository.getReferenceById(id)).thenReturn(existingProduct);
		when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
		ProductDTO resultDto = productService.update(id, inputDto);
		verify(productRepository, times(1)).getReferenceById(id);
		verify(productRepository, times(1)).save(any(Product.class));

		assertNotNull(resultDto);
		assertEquals(existingProduct.getId(), resultDto.getId());
		assertEquals(inputDto.getName(), resultDto.getName());
		assertEquals(inputDto.getDescription(), resultDto.getDescription());

	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
		Long id = 100L;
		when(productRepository.getReferenceById(id)).thenThrow(new EntityNotFoundException());
		assertThrows(ResourceNotFoundException.class, () -> productService.update(id, new ProductDTO()));
	}

	@Test
	public void findAllPageShouldReturnPage() {
		String nameProduct = "Playstation";
		String categoryId = "1,2";
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		ProductService productServiceSpy = Mockito.spy(productService);
		Page<ProductDTO> result = productServiceSpy.findAllPaged(categoryId, nameProduct, pageable);
		assertNotNull(result);
		verify(productRepository).searchProducts(categoryIds, nameProduct, pageable);
		verify(productRepository).searchProductsWithCategories(productIds);
	}

	@Test
	public void findByIdShouldReturnProductDTO() {
		ProductService productServiceSpy = Mockito.spy(productService);
		ProductDTO result = productServiceSpy.findById(existingId);
		assertNotNull(result);
		verify(productRepository).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		ProductService productServiceSpy = Mockito.spy(productService);
		assertThrows(ResourceNotFoundException.class, () -> {
			productServiceSpy.findById(notExistingId);
		});
	}

	@Test
	public void deleteShouldThrowsResourceNotFoundException() {
		when(productRepository.existsById(notExistingId)).thenReturn(false);
		assertThrows(ResourceNotFoundException.class, () -> productService.delete(notExistingId));
		verify(productRepository, times(1)).existsById(notExistingId);
		verify(productRepository, never()).deleteById(notExistingId);
	}

	@Test
	public void deleteShouldThrowsDataIntegrityViolationExceptioThrowsDatabaseException() {
		when(productRepository.existsById(existingId)).thenReturn(true);
		doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(existingId);
		assertThrows(DatabaseException.class, () -> productService.delete(existingId));
		verify(productRepository, times(1)).existsById(existingId);
		verify(productRepository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldNotNothingWhenIdExists() {
		ProductService productServiceSpy = Mockito.spy(productService);
		Assertions.assertDoesNotThrow(() -> {
			productServiceSpy.delete(existingId);
		});
	}

	@Test
	public void findAllPageShouldReturnPageWhenPage0Size10() {

		String nameProduct = "Playstation";
		String categoryId = "1,2";
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		ProductService productServiceSpy = Mockito.spy(productService);
		Page<ProductDTO> result = productServiceSpy.findAllPaged(categoryId, nameProduct, pageable);

		assertNotNull(result);
		assertTrue(true, String.valueOf(result.isFirst()));
		assertEquals(0, result.getNumber());
		assertEquals(1, result.getTotalElements());
		assertEquals(3, result.getSize());
		verify(productRepository).searchProducts(categoryIds, nameProduct, pageable);
		verify(productRepository).searchProductsWithCategories(productIds);
	}

	@Test
	public void testCopyDtoToEntity() {
		ProductDTO inputDto = new ProductDTO();
		inputDto.setName("Test Product");
		inputDto.setDescription("Test Description");

		CategoryDTO cat = new CategoryDTO(1L, "Eletronics");
		List<CategoryDTO> listCatDTO = List.of(cat);
		inputDto.setCategories(listCatDTO);

		Product entity = new Product();
		productService.acessoAoMetodo(inputDto, entity);
		assertEquals(inputDto.getName(), entity.getName());
		assertEquals(inputDto.getDescription(), entity.getDescription());
		assertEquals(inputDto.getCategories().get(0).getName(), "Eletronics");
	}

}
