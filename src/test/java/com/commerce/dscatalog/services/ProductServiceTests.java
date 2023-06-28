package com.commerce.dscatalog.services;

import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.repositories.CategoryRepository;
import com.commerce.dscatalog.repositories.ProductRepository;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


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
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 4;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(notExistingId)).thenReturn(Optional.empty());
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.existsById(notExistingId)).thenReturn(false);
        when(productRepository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void findAllPageShouldReturnPage() {
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);
        assertNotNull(result);
        verify(productRepository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTO() {
        ProductDTO result = productService.findById(existingId);
        assertNotNull(result);
        verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(notExistingId);
        });
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

//        assertThrows(ResourceNotFoundException.class, () -> {
//            productService.update(notExistingId,productDTO);
//        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldNotNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });
    }

}