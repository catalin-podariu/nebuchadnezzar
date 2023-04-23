package com.big.store.nebuchadnezzar.controller;

import com.big.store.nebuchadnezzar.dto.Product;
import com.big.store.nebuchadnezzar.model.ProductModel;
import com.big.store.nebuchadnezzar.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(productService.getProductById(id), Product.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts().stream()
                .map(productModel -> modelMapper.map(productModel, Product.class))
                .collect(Collectors.toList()));
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        ProductModel productModel = productService.createProduct(modelMapper.map(product, ProductModel.class));
        return ResponseEntity.ok(modelMapper.map(productModel, Product.class));
    }

    @PatchMapping("/updateName/{id}")
    public ResponseEntity<Product> updateProductName(@PathVariable Long id, @RequestParam String name) {
        ProductModel productModel = productService.updateProductName(id, name);
        return ResponseEntity.ok(modelMapper.map(productModel, Product.class));
    }

    @PatchMapping("/updatePrice/{id}")
    public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @RequestParam BigDecimal price) {
        ProductModel productModel = productService.updateProductPrice(id, price);
        Product productDTO = modelMapper.map(productModel, Product.class);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
