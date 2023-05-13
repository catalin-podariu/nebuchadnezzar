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
        return findProductById(id);
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }
    public ProductModel createProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    public ProductModel updateProductName(Long id, String name) {
        ProductModel productModel = findProductById(id);
        productModel.setName(name);
        return productRepository.save(productModel);
    }

    public ProductModel updateProduct(Long id, ProductModel productModel) {
        ProductModel product = findProductById(id);
        product.setName(productModel.getName());
        product.setPrice(productModel.getPrice());
        product.setStock(productModel.getStock());
        product.setDescription(productModel.getDescription());
        product.setImage(productModel.getImage());
        return productRepository.save(product);
    }

    public ProductModel updateProductPrice(Long id, BigDecimal price) {
        ProductModel productModel = findProductById(id);
        productModel.setPrice(price);
        return productRepository.save(productModel);
    }

    public void deleteProduct(Long id) {
        productRepository.delete(findProductById(id));
    }

    private ProductModel findProductById(Long id) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found with id: " + id);
        }
        return productOptional.get();
    }

    public ProductModel updateProductStock(Long id, Integer stock) {
        ProductModel productModel = findProductById(id);
        productModel.setStock(stock);
        return productRepository.save(productModel);
    }

    public ProductModel updateProductDescription(Long id, String description) {
        ProductModel productModel = findProductById(id);
        productModel.setDescription(description);
        return productRepository.save(productModel);
    }

    public ProductModel updateProductImage(Long id, String image) {
        ProductModel productModel = findProductById(id);
        productModel.setImage(image);
        return productRepository.save(productModel);
    }

    public ProductModel updateProductCategory(Long id, String category) {
        ProductModel productModel = findProductById(id);
        productModel.setCategory(category);
        return productRepository.save(productModel);
    }

    public ProductModel updateProductBrand(Long id, String brand) {
        ProductModel productModel = findProductById(id);
        productModel.setBrand(brand);
        return productRepository.save(productModel);
    }
}
