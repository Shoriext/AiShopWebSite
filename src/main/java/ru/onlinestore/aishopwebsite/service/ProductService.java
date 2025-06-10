package ru.onlinestore.aishopwebsite.service;

import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Получить все товары
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Получить товар по ID
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Добавить новый товар
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Обновить существующий товар
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existingProduct);
    }

    /**
     * Удалить товар по ID
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}
