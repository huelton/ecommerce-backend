package com.commerce.dscatalog.repositories;

import com.commerce.dscatalog.entities.Category;
import com.commerce.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    private long existingId;
    private long notExistingId;
    private long countTotalCategory;

    @BeforeEach
    void setup() {
        existingId = 1L;
        notExistingId = 1000L;
        countTotalCategory = 3;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Category category = Factory.createCategory();
        category.setId(null);

        category = categoryRepository.save(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(countTotalCategory + 1, category.getId());
    }

    @Test
    public void findShouldReturnCategoryWhenIdExists() {
        Optional<Category> result = categoryRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findShouldNotReturnCategoryWhenIdNotExists() {
        Optional<Category> result = categoryRepository.findById(notExistingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists() {
        categoryRepository.deleteById(existingId);
        Optional<Category> result = categoryRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
}
