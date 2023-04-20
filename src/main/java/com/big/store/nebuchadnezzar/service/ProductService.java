package com.big.store.nebuchadnezzar.service;

import com.big.store.nebuchadnezzar.constant.ErrorCode;
import com.big.store.nebuchadnezzar.exception.ProductNotFoundException;
import com.big.store.nebuchadnezzar.model.ProductModel;
import com.big.store.nebuchadnezzar.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductModel getProductById(Long id) {
        return findById(id);
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }
    public ProductModel createProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    public ProductModel updateProductName(Long id, String name) {
        ProductModel productModel = findById(id);
        productModel.setName(name);
        return productRepository.save(productModel);
    }

    public ProductModel updateProductPrice(Long id, BigDecimal price) {
        ProductModel productModel = findById(id);
        productModel.setPrice(price);
        return productRepository.save(productModel);
    }

    public void deleteProduct(Long id) {
        productRepository.delete(findById(id));
    }

    private ProductModel findById(Long id) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found with id: " + id);
        }
        return productOptional.get();
    }
}
