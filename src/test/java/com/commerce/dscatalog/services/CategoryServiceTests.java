package com.commerce.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.entities.Category;
import com.commerce.dscatalog.repositories.CategoryRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@Mock
	private CategoryRepository repository;

	@InjectMocks
	private CategoryService categoryService;

	private long existingId;
	private long notExistingId;
	private long dependentId;
	private List<Category> categories;
	private Category category, savedCategory, savedInsertCategory, updatedCategory;
	private CategoryDTO dtoToInsert, dtoToUpdate;

	private PageImpl<Category> page;

	@BeforeEach
	void setup() {
		existingId = 1L;
		notExistingId = 1000L;
		dependentId = 4;
		categories = List.of(Factory.createCategory(), Factory.createCategory());
		page = new PageImpl<>(List.of(Factory.createCategory(), Factory.createCategory()));
		category = Factory.createCategory();
		savedCategory = Factory.createCategory();
		savedInsertCategory = Factory.createCategory();
		dtoToUpdate = Factory.createCategoryDTOToUpdate();

		when(repository.findAll()).thenReturn(categories);

		when(repository.findById(existingId)).thenReturn(Optional.of(category));

		when(repository.findById(notExistingId)).thenReturn(Optional.empty());

		when(repository.getReferenceById(notExistingId)).thenThrow(new EntityNotFoundException());

		when(repository.save(any(Category.class))).thenReturn(savedInsertCategory);
		when(repository.save(any(Category.class))).thenReturn(savedCategory);

		when(repository.getReferenceById(existingId)).thenReturn(category);

		updatedCategory = Factory.createCategoryToUpdate();
		when(repository.save(any(Category.class))).thenReturn(updatedCategory);

		dtoToInsert = Factory.createCategoryDTOToInsert();

		when(repository.existsById(existingId)).thenReturn(true);
		when(repository.existsById(notExistingId)).thenReturn(false);
		when(repository.existsById(dependentId)).thenReturn(true);
	}

	@Test
	public void findAllShouldReturnWhenCategoriesExists() {
		List<CategoryDTO> result = categoryService.findAll();
		verify(repository, times(1)).findAll();
		assertEquals(categories.size(), result.size());
	}

	@Test
	public void findAllPagedShouldReturnPaginationWhenCategoriesExists() {
		when(repository.findAll(any(Pageable.class))).thenReturn(page);
		Page<CategoryDTO> resultPage = categoryService.findAllPaged(Pageable.unpaged());
		verify(repository, times(1)).findAll(any(Pageable.class));
		assertEquals(page.getTotalElements(), resultPage.getTotalElements());
	}

	@Test
	public void findByIdShouldReturnWhenCategoriesExists() {
		CategoryDTO result = categoryService.findById(existingId);
		verify(repository, times(1)).findById(existingId);
		assertEquals(category.getId(), result.getId());
		assertEquals(category.getName(), result.getName());
	}

	@Test
	public void findByIdShoulThrowResourceNotFoundExceptionwhenDoesNotExistId() {
		assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(notExistingId));
		verify(repository, times(1)).findById(notExistingId);
	}

	@Test
	public void insertShouldReturnDataWhenCategoryInserted() {
		// Ncessario dentro do metodo para não dar comflito com o teste de update
		// categoria
		when(repository.save(any(Category.class))).thenReturn(savedCategory);
		CategoryDTO result = categoryService.insert(dtoToInsert);
		verify(repository, times(1)).save(any(Category.class));
		assertEquals(dtoToInsert.getName(), result.getName());
	}

	@Test
	public void updateShouldUpdateDataWhenCategoryIdExist() {
		CategoryDTO result = categoryService.update(existingId, dtoToUpdate);
		verify(repository, times(1)).getReferenceById(existingId);
		verify(repository, times(1)).save(any(Category.class));
		assertEquals(existingId, result.getId());
		assertEquals(dtoToUpdate.getName(), result.getName());
	}

	@Test
	public void updateShouldNotUpdateThrowsResourceNotFoundExceptionWhenIdNotExist() {
		CategoryDTO dtoToUpdate = new CategoryDTO(null, "Updated Category");
		assertThrows(ResourceNotFoundException.class, () -> categoryService.update(notExistingId, dtoToUpdate));
		verify(repository, times(1)).getReferenceById(notExistingId);
		verify(repository, never()).save(any(Category.class));
	}

	@Test
	public void deleteShouldNotNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			categoryService.delete(existingId);
		});
	}

	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionAndDatabaseException() {
		Long idToDelete = 1L;
		when(repository.existsById(idToDelete)).thenReturn(true);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(idToDelete);
		assertThrows(DatabaseException.class, () -> categoryService.delete(idToDelete));
		verify(repository, times(1)).existsById(idToDelete);
		verify(repository, times(1)).deleteById(idToDelete);
	}

}
