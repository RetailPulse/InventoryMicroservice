package com.retailpulse.service;

import com.retailpulse.entity.Product;
import com.retailpulse.repository.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ProductService {

    private static final String PRODUCT_NOT_FOUND_DESC = "Product not found with id: ";

    private final SKUGeneratorService skuGeneratorService;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Autowired
    public ProductService(SKUGeneratorService skuGeneratorService, ProductRepository productRepository, InventoryService inventoryService) {
        this.skuGeneratorService = skuGeneratorService;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySKU(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional
    public Product saveProduct(@NotNull Product product) {
        if (product.getRrp() < 0) {
            throw new IllegalArgumentException("Recommended retail price cannot be negative");
        }
        // Generate SKU before saving
        String generatedSKU = skuGeneratorService.generateSKU();
        product.setSku(generatedSKU);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_DESC + id));

        if (!product.isActive()) {
            throw new RuntimeException("Cannot update a deleted product with id: " + id);
        }

        updateField(productDetails.getDescription(), product::setDescription);
        updateField(productDetails.getCategory(), product::setCategory);
        updateField(productDetails.getSubcategory(), product::setSubcategory);
        updateField(productDetails.getBrand(), product::setBrand);
        updateField(productDetails.getOrigin(), product::setOrigin);
        updateField(productDetails.getUom(), product::setUom);
        updateField(productDetails.getVendorCode(), product::setVendorCode);
        updateField(productDetails.getBarcode(), product::setBarcode);
        if (productDetails.getRrp() >= 0) {
            product.setRrp(productDetails.getRrp());
        }

        // Do not update isActive field, this is used for soft delete
        // product.setIsActive(productDetails.isActive());

        return productRepository.save(product);
    }

    // Generic helper method for updating fields
    private <T> void updateField(T newValue, Consumer<T> updater) {
        if(newValue == null) {
            return;
        }
        if (newValue instanceof String && ((String) newValue).isEmpty()) {
            return;
        }
        updater.accept(newValue);
    }

    public Product softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_DESC + id));

        if (!product.isActive()) {
            throw new IllegalArgumentException("Product with id " + id + " is already deleted.");
        }

        if (inventoryService.inventoryContainsProduct(product.getId())) {
            throw new IllegalStateException("Cannot delete product with id " + id + " because it exists in inventory.");
        }
        product.setActive(false);
        return productRepository.save(product);
    }

    public Product reverseSoftDelete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_DESC + id));

        updateField(true, product::setActive);

        return productRepository.save(product);
    }

}
