package com.example.capstone_db.service

import com.example.capstone_db.model.Product
import com.example.capstone_db.repository.ProductRepository
import com.example.capstone_db.viewmodel.ProductViewModel
import com.example.capstone_db.viewmodel.convertToProductViewModel
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun addProduct(product: Product): Product? {
        return productRepository.findByproductName(product.productName) ?: productRepository.save(product)
    }

    fun getProducts(): List<ProductViewModel>? {
        val products = productRepository.findAll()
        return products.map { convertToProductViewModel(it) }
    }

    fun findProductById(productId: Long): ProductViewModel? {
        val product = productRepository.findByIdOrNull(productId)
        return product?.let { convertToProductViewModel(it) }
    }

    fun findProductsByCategory(category: String): List<ProductViewModel>? {
        val products = productRepository.findByCategory(category)
        return products?.map { convertToProductViewModel(it) }
    }

    fun findProductsBetweenPrice(minPrice: Double, maxPrice: Double): List<ProductViewModel>? {
        val products = productRepository.findProductsBetweenPrice(minPrice, maxPrice)
        return products?.map { convertToProductViewModel(it) }
    }

    fun deleteProductById(productId: Long) {
        return productRepository.deleteById(productId)
    }

    @Transactional
    fun updateProductById(productId: Long, productViewModel: ProductViewModel) {
        val rowsUpdated = productRepository.updateProduct(
            productId = productId,
            productName = productViewModel.productName,
            productPrize = productViewModel.productPrize,
            category = productViewModel.category
        )

        if (rowsUpdated == 0) {
            productRepository.saveProductWithId(
                    productId,
                productViewModel.productName,
                productViewModel.productPrize,
                productViewModel.category
            )
        }
    }

    fun getProductByName(productName: String): ProductViewModel? {
        val product = productRepository.findByproductName(productName)
        return product?.let { convertToProductViewModel(it) }
    }

    fun searchProducts(product: String): ResponseEntity<List<ProductViewModel>> {
        val products = productRepository.findProductByProductNameContaining(product)
        return ResponseEntity.ok(products?.map { convertToProductViewModel(it) })
    }
}