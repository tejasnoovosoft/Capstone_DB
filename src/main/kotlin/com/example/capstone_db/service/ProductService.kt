package com.example.capstone_db.service

import com.example.capstone_db.model.Product
import com.example.capstone_db.repository.ProductRepository
import com.example.capstone_db.viewmodel.ProductDTO
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun addProduct(product: Product): Product? {
        return productRepository.findByproductName(product.productName) ?: productRepository.save(product)
    }

    fun getProducts(): List<Product>? {
        return productRepository.findAll()
    }

    fun findProductById(productId: Long): Product? {
        return productRepository.findByIdOrNull(productId)
    }

    fun findProductsByCategory(category: String): List<Product>? {
        return productRepository.findByCategory(category)
    }

    fun findProductsBetweenPrice(minPrice: Double, maxPrice: Double): List<Product>? {
        return productRepository.findProductsBetweenPrice(minPrice, maxPrice)
    }

    fun deleteProductById(productId: Long) {
        return productRepository.deleteById(productId)
    }

    @Transactional
    fun updateProductById(productId: Long, productDTO: ProductDTO) {
        val rowsUpdated = productRepository.updateProduct(
            productId = productId,
            productName = productDTO.productName,
            productPrize = productDTO.productPrize,
            category = productDTO.category
        )

        if (rowsUpdated == 0) {
//            productRepository.saveProductWithId(
            productRepository.save(
                Product(
                    productId,
                    productDTO.productName,
                    productDTO.productPrize,
                    productDTO.category
                )
            )
        }
    }
}