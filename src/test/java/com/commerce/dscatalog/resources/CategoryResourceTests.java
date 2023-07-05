package com.commerce.dscatalog.resources;

import com.commerce.dscatalog.dto.CategoryDTO;
import com.commerce.dscatalog.dto.ProductDTO;
import com.commerce.dscatalog.services.CategoryService;
import com.commerce.dscatalog.services.ProductService;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@WebMvcTest(CategoryResource.class)
public class CategoryResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    private CategoryDTO categoryDTO;
    @Autowired
    private ObjectMapper objectMapper;
    private long existingId;
    private long notExistingId;
    private long dependentId;
    private PageImpl<CategoryDTO> page;
    @Disabled
    @BeforeEach
    void setup() {
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 3L;
        categoryDTO = Factory.createCategoryDTO();
        page = new PageImpl<>(List.of(categoryDTO));

        when(categoryService.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        when(categoryService.findById(existingId)).thenReturn(categoryDTO);
        when(categoryService.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

        when(categoryService.update(eq(existingId), ArgumentMatchers.any())).thenReturn(categoryDTO);
        when(categoryService.update(eq(notExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(categoryService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(categoryService).delete(notExistingId);
        doThrow(DatabaseException.class).when(categoryService).delete(dependentId);

        when(categoryService.insert(ArgumentMatchers.any())).thenReturn(categoryDTO);

    }
    @Disabled
    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Disabled
    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }
    @Disabled
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", notExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Disabled
    @Test
    public void insertShouldReturnCreatedAndProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

    }
    @Disabled
    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

    }
    @Disabled
    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(categoryDTO);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", notExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Disabled
    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    @Disabled
    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
