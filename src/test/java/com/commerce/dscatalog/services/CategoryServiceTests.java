package com.commerce.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.entities.Category;
import com.commerce.dscatalog.repositories.CategoryRepository;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.tests.Factory;


@SpringBootTest
@Transactional
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    private PageImpl<Category> page;
    private long existingId;
    private long notExistingId;
    private long dependentId;
    private Category category;

    @BeforeEach
    void setup() {
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 4;
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(category);
        when(categoryRepository.findById(existingId)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(notExistingId)).thenReturn(Optional.empty());
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).deleteById(dependentId);

        when(categoryRepository.existsById(existingId)).thenReturn(true);
        when(categoryRepository.existsById(notExistingId)).thenReturn(false);
        when(categoryRepository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void findAllPageShouldReturnPage() {
        Pageable pageable = PageRequest.of(0,10);
        Page<CategoryDTO> result = categoryService.findAllPaged(pageable);
        assertNotNull(result);
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTO() {
        CategoryDTO result = categoryService.findById(existingId);
        assertNotNull(result);
        verify(categoryRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findById(notExistingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldNotNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            categoryService.delete(existingId);
        });
    }

    @Test
    public void findAllPageShouldReturnPageWhenPage0Size10() {
        Pageable pageable = PageRequest.of(0,10);
        Page<CategoryDTO> result = categoryService.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertNotNull(result);
        assertEquals(0, result.getNumber());
        assertTrue(true, String.valueOf(result.isFirst()));
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository).findAll(pageable);
    }

}
