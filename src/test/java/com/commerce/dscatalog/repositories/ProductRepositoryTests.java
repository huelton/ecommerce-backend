package com.commerce.dscatalog.repositories;

import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    private long existingId;
    private long notExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setup(){
        existingId = 1L;
        notExistingId = 1000L;
        countTotalProducts = 25;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenidIsNull(){
         Product product = Factory.createProduct();
         product.setId(null);

         product = productRepository.save(product);
         Assertions.assertNotNull(product.getId());
         Assertions.assertEquals(countTotalProducts+1, product.getId());
    }

    @Test
    public void findShouldReturnProductWhenIdExists(){
        Optional<Product> result =  productRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findShouldNotReturnProductWhenIdNotExists(){
        Optional<Product> result =  productRepository.findById(notExistingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldDeleteObjectWhenidExists(){
        productRepository.deleteById(existingId);
        Optional<Product> result =  productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
}
