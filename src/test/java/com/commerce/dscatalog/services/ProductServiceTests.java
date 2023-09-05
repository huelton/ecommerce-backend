package com.commerce.dscatalog.services;

import static com.commerce.dscatalog.tests.Constants.PAGE;
import static com.commerce.dscatalog.tests.Constants.SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import com.commerce.dscatalog.tests.TestProductProjection;

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
	private String nameProduct, categoryId;
	Pageable pageable;

	@BeforeEach
	void setup() {
		nameProduct = "Playstation";
		categoryId = "1,2";
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
		pageable = PageRequest.of(PAGE, SIZE);
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
		productService.accessMethod(inputDto, savedProduct);
		ProductService productServiceSpy = Mockito.spy(productService);
		ProductDTO resultDto = productServiceSpy.insert(inputDto);
		verify(productRepository, times(1)).save(any(Product.class));

		assertNotNull(resultDto);
		assertEquals(savedProduct.getId(), resultDto.getId());
		assertEquals(inputDto.getName(), resultDto.getName());
		assertEquals(inputDto.getDescription(), resultDto.getDescription());
	}

	@Test
	public void updateProductShouldReturnProductDTO() {
		ProductDTO inputDto = new ProductDTO();
		inputDto.setName("Updated Product");
		inputDto.setDescription("Updated Description");
		Product existingProduct = new Product();
		existingProduct.setId(existingId);

		when(productRepository.getReferenceById(existingId)).thenReturn(existingProduct);
		when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
		ProductService productServiceSpy = Mockito.spy(productService);
		ProductDTO resultDto = productServiceSpy.update(existingId, inputDto);
		verify(productRepository, times(1)).getReferenceById(existingId);
		verify(productRepository, times(1)).save(any(Product.class));

		assertNotNull(resultDto);
		assertEquals(existingProduct.getId(), resultDto.getId());
		assertEquals(inputDto.getName(), resultDto.getName());
		assertEquals(inputDto.getDescription(), resultDto.getDescription());

	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
		when(productRepository.getReferenceById(notExistingId)).thenThrow(new EntityNotFoundException());
		assertThrows(ResourceNotFoundException.class, () -> productService.update(notExistingId, new ProductDTO()));
	}

	@Test
	public void findAllPageShouldReturnPage() {
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
		assertThrows(ResourceNotFoundException.class, () -> productService.delete(notExistingId));
		verify(productRepository, times(1)).existsById(notExistingId);
		verify(productRepository, never()).deleteById(notExistingId);
	}

	@Test
	public void deleteShouldThrowsDataIntegrityViolationExceptioThrowsDatabaseException() {
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
	public void shouldValidadeTestCopyDtoToEntity() {
		ProductDTO inputDto = new ProductDTO();
		inputDto.setName("Test Product");
		inputDto.setDescription("Test Description");

		CategoryDTO cat = new CategoryDTO(1L, "Eletronics");
		List<CategoryDTO> listCatDTO = List.of(cat);
		inputDto.setCategories(listCatDTO);

		Product entity = new Product();
		productService.accessMethod(inputDto, entity);
		assertEquals(inputDto.getName(), entity.getName());
		assertEquals(inputDto.getDescription(), entity.getDescription());
		assertEquals(inputDto.getCategories().get(0).getName(), "Eletronics");
	}

	@Test
	public void shouldFindAllPagedNoFilterNoMatchingProducts() {
		String categoryId = "0";
		String name = null;
		Pageable pageable = Pageable.ofSize(10).withPage(0);

		when(productRepository.searchProducts(anyList(), eq(null), eq(pageable)))
				.thenReturn(new PageImpl<>(Arrays.asList()));

		Page<ProductDTO> result = productService.findAllPaged(categoryId, name, pageable);

		verify(productRepository).searchProducts(anyList(), eq(null), eq(pageable));

		assertEquals(0, result.getTotalElements());
	}

	@Test
	public void shouldFindAllPagedNoFilterMatchingProducts() {

		String categoryId = "0";
		String name = null;
		Pageable pageable = Pageable.ofSize(10).withPage(0);

		var p1 = new Product(1L, "Product 1");
		var p2 = new Product(2L, "Product 2");

		var productProjectionP1 = new TestProductProjection();
		var productProjectionP2 = new TestProductProjection();

		productProjectionP1.insertData(p1);
		productProjectionP2.insertData(p2);

		List<ProductProjection> productProjections = Arrays.asList(productProjectionP1, productProjectionP2);

		when(productRepository.searchProducts(anyList(), eq(null), eq(pageable)))
				.thenReturn(new PageImpl<>(productProjections));

		when(productRepository.searchProductsWithCategories(anyList())).thenReturn(Arrays.asList(p1,p2));

		Page<ProductDTO> result = productService.findAllPaged(categoryId, name, pageable);
		verify(productRepository).searchProducts(anyList(), eq(null), eq(pageable));
		verify(productRepository).searchProductsWithCategories(Arrays.asList(1L, 2L));

		assertEquals(2, result.getTotalElements());
	}

}
