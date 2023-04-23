package com.big.store.nebuchadnezzar.controller;

import com.big.store.nebuchadnezzar.constant.ErrorCode;
import com.big.store.nebuchadnezzar.dto.Product;
import com.big.store.nebuchadnezzar.exception.ProductNotFoundException;
import com.big.store.nebuchadnezzar.model.ProductModel;
import com.big.store.nebuchadnezzar.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@ContextConfiguration
@WithMockUser(username = "user", roles = {"USER", "ADMIN"})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void test_GET_ProductByIdSuccess() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductModel productModel = new ProductModel(productId, "Test Product", BigDecimal.valueOf(9.99));
        Product expectedProduct = new Product(productId, "Test Product", BigDecimal.valueOf(9.99));

        when(productService.getProductById(productId)).thenReturn(productModel);
        when(modelMapper.map(productModel, Product.class)).thenReturn(expectedProduct);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/get/" + productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(9.99))
                .andReturn();

        // Assert
        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("{\"id\":1,\"name\":\"Test Product\",\"price\":9.99}");
    }

    @Test
    void test_GET_ProductByIdNotFound() throws Exception {
        // Arrange
        Long productId = -1L;
        when(productService.getProductById(productId))
                .thenThrow(new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found with id: " + productId));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/products/get/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void test_GET_AllProductsSuccess() throws Exception {
        // Arrange
        List<ProductModel> productModels = new ArrayList<>();
        productModels.add(new ProductModel(1L, "Test Product 1", BigDecimal.valueOf(9.99)));
        productModels.add(new ProductModel(2L, "Test Product 2", BigDecimal.valueOf(19.99)));
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product(1L, "Test Product 1", BigDecimal.valueOf(9.99)));
        expectedProducts.add(new Product(2L, "Test Product 2", BigDecimal.valueOf(19.99)));

        when(productService.getAllProducts()).thenReturn(productModels);
        when(modelMapper.map(any(), any())).thenReturn(expectedProducts.get(0), expectedProducts.get(1));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/products/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(9.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Test Product 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(19.99))
                .andReturn();
    }

    @Test
    void test_POST_CreateProductSuccess() throws Exception {
        // Arrange
        Product product = new Product(null, "Test Product", BigDecimal.valueOf(9.99));
        ProductModel productModel = new ProductModel(1001L, "Test Product", BigDecimal.valueOf(9.99));

        when(productService.createProduct(any())).thenReturn(productModel);
        when(modelMapper.map(product, ProductModel.class)).thenReturn(productModel);
        when(modelMapper.map(productModel, Product.class)).thenReturn(new Product(1001L, "Test Product", BigDecimal.valueOf(9.99)));

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/products/create")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"price\":9.99}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1001L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(9.99))
                .andReturn();

        // Assert
        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("{\"id\":1,\"name\":\"Test Product\",\"price\":9.99}");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_PATCH_UpdateProductNameSuccess() throws Exception {
        // Arrange
        Long productId = 1L;
        String newName = "New Test Product";
        ProductModel productModel = new ProductModel(productId, newName, BigDecimal.valueOf(9.99));
        Product expectedProduct = new Product(productId, newName, BigDecimal.valueOf(9.99));

        when(productService.updateProductName(productId, newName)).thenReturn(productModel);
        when(modelMapper.map(productModel, Product.class)).thenReturn(expectedProduct);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/products/updateName/" + productId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", newName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(9.99))
                .andReturn();

        // Assert
        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("{\"id\":1,\"name\":\"New Test Product\",\"price\":9.99}");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_PATCH_UpdateProductPriceSuccess() throws Exception {
        // Arrange
        Long productId = 1L;
        BigDecimal newPrice = BigDecimal.valueOf(19.99);
        ProductModel productModel = new ProductModel(productId, "Test Product", newPrice);
        Product expectedProduct = new Product(productId, "Test Product", newPrice);

        when(productService.updateProductPrice(productId, newPrice)).thenReturn(productModel);
        when(modelMapper.map(productModel, Product.class)).thenReturn(expectedProduct);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/products/updatePrice/" + productId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("price", "19.99"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(19.99))
                .andReturn();

        // Assert
        String responseString = result.getResponse().getContentAsString();
        assertThat(responseString).isEqualTo("{\"id\":1,\"name\":\"Test Product\",\"price\":19.99}");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_DELETE_DeleteProductSuccess() throws Exception {
        // Arrange
        long productId = 1L;
        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/" + productId))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}
