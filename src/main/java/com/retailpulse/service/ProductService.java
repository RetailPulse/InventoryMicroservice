package com.retailpulse.service;

import com.retailpulse.dto.response.ProductResponseDto;
import com.retailpulse.entity.Product;
import com.retailpulse.repository.ProductRepository;
import com.retailpulse.service.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ProductService {

    private static final String PRODUCT_BY_ID_NOT_FOUND = "PRODUCT_BY_ID_NOT_FOUND";
    private static final String PRODUCT_BY_SKU_NOT_FOUND = "PRODUCT_BY_SKU_NOT_FOUND";
    private static final String PRODUCT_BY_ID_NOT_FOUND_DESC = "Product by Id not found with id: ";
    private static final String PRODUCT_BY_SKU_NOT_FOUND_DESC = "Product by SKU not found with id: ";

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

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getSku(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getSubcategory(),
                        product.getBrand(),
                        product.getOrigin(),
                        product.getUom(),
                        product.getVendorCode(),
                        product.getBarcode(),
                        product.getRrp(),
                        product.isActive()
                ))
                .toList();
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PRODUCT_BY_ID_NOT_FOUND, PRODUCT_BY_ID_NOT_FOUND_DESC + id));

        return new ProductResponseDto(
                product.getId(),
                product.getSku(),
                product.getDescription(),
                product.getCategory(),
                product.getSubcategory(),
                product.getBrand(),
                product.getOrigin(),
                product.getUom(),
                product.getVendorCode(),
                product.getBarcode(),
                product.getRrp(),
                product.isActive()
        );
    }

    public ProductResponseDto getProductBySKU(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new BusinessException(PRODUCT_BY_SKU_NOT_FOUND, PRODUCT_BY_SKU_NOT_FOUND_DESC + sku));

        return new ProductResponseDto(
                product.getId(),
                product.getSku(),
                product.getDescription(),
                product.getCategory(),
                product.getSubcategory(),
                product.getBrand(),
                product.getOrigin(),
                product.getUom(),
                product.getVendorCode(),
                product.getBarcode(),
                product.getRrp(),
                product.isActive()
        );
    }

    @Transactional
    public ProductResponseDto saveProduct(@NotNull Product product) {
        if (product.getRrp() < 0) {
            throw new IllegalArgumentException("Recommended retail price cannot be negative");
        }
        // Generate SKU before saving
        String generatedSKU = skuGeneratorService.generateSKU();
        product.setSku(generatedSKU);
        Product createdProduct = productRepository.save(product);
        return new ProductResponseDto(
                createdProduct.getId(),
                createdProduct.getSku(),
                createdProduct.getDescription(),
                createdProduct.getCategory(),
                createdProduct.getSubcategory(),
                createdProduct.getBrand(),
                createdProduct.getOrigin(),
                createdProduct.getUom(),
                createdProduct.getVendorCode(),
                createdProduct.getBarcode(),
                createdProduct.getRrp(),
                createdProduct.isActive()
        );
    }

    public ProductResponseDto updateProduct(Long id, Product productDetails) {
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

        Product updatedProduct = productRepository.save(product);
        return new ProductResponseDto(
                updatedProduct.getId(),
                updatedProduct.getSku(),
                updatedProduct.getDescription(),
                updatedProduct.getCategory(),
                updatedProduct.getSubcategory(),
                updatedProduct.getBrand(),
                updatedProduct.getOrigin(),
                updatedProduct.getUom(),
                updatedProduct.getVendorCode(),
                updatedProduct.getBarcode(),
                updatedProduct.getRrp(),
                updatedProduct.isActive()
        );
    }

    // Generic helper method for updating fields
    private <T> void updateField(T newValue, Consumer<T> updater) {
        if (newValue == null) {
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

    public ProductResponseDto reverseSoftDelete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_DESC + id));

        updateField(true, product::setActive);

        Product updatedProduct = productRepository.save(product);
        return new ProductResponseDto(
                updatedProduct.getId(),
                updatedProduct.getSku(),
                updatedProduct.getDescription(),
                updatedProduct.getCategory(),
                updatedProduct.getSubcategory(),
                updatedProduct.getBrand(),
                updatedProduct.getOrigin(),
                updatedProduct.getUom(),
                updatedProduct.getVendorCode(),
                updatedProduct.getBarcode(),
                updatedProduct.getRrp(),
                updatedProduct.isActive()
        );
    }

}
