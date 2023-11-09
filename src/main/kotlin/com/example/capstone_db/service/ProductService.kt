package com.example.capstone_db.service

import com.example.capstone_db.model.Product
import com.example.capstone_db.repository.ProductRepository
import com.example.capstone_db.viewmodel.ProductOutputViewModel
import com.example.capstone_db.viewmodel.ProductViewModel
import com.example.capstone_db.viewmodel.convertToProductOutputViewModel
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val firebaseStorageService: FirebaseStorageService,
    @Qualifier("threadPoolTaskExecutor") private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {
    @Async("threadPoolTaskExecutor")
    fun addProducts(productItems: List<ProductViewModel>) {
        for (productItem in productItems) {
            CompletableFuture.runAsync({
                println(Thread.currentThread().name)
                val (productName, productPrize, category, images) = productItem
                val imageUrls = images.map { imagePath ->
                    val file = File(imagePath)
                    val imageBytes = Files.readAllBytes(file.toPath())
                    val base64EncodedString = Base64.getEncoder().encodeToString(imageBytes)
                    val imageName = file.name
                    firebaseStorageService.uploadImage(base64EncodedString, imageName)
                }
                val product =
                    Product(
                        productName = productName,
                        productPrize = productPrize,
                        category = category,
                        image = imageUrls
                    )
                productRepository.save(product)
            }, threadPoolTaskExecutor)
        }
    }

    fun getProducts(): List<ProductOutputViewModel>? {
        val products = productRepository.findAll()
        return products.map { convertToProductOutputViewModel(it) }
    }

    fun findProductById(productId: Long): ProductOutputViewModel? {
        val product = productRepository.findByIdOrNull(productId)
        return product?.let { convertToProductOutputViewModel(it) }
    }

    fun findProductsByCategory(category: String): List<ProductOutputViewModel>? {
        val products = productRepository.findByCategory(category)
        return products?.map { convertToProductOutputViewModel(it) }
    }

    fun findProductsBetweenPrice(minPrice: Double, maxPrice: Double): List<ProductOutputViewModel>? {
        val products = productRepository.findProductsByProductPrizeBetween(minPrice, maxPrice)
        return products?.map { convertToProductOutputViewModel(it) }
    }

    fun deleteProductById(productId: Long) {
        return productRepository.deleteById(productId)
    }

    @Transactional
    fun updateProductById(productId: Long, productViewModel: ProductOutputViewModel) {
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

    fun getProductByName(productName: String): ProductOutputViewModel? {
        val product = productRepository.findByproductName(productName)
        return product?.let { convertToProductOutputViewModel(it) }
    }

    fun searchProducts(product: String): ResponseEntity<List<ProductOutputViewModel>> {
        val products = productRepository.findProductByProductNameContaining(product)
        return ResponseEntity.ok(products?.map { convertToProductOutputViewModel(it) })
    }

    fun getProductImagesById(productId: Long): List<String>? {
        return productRepository.getProductImageUrls(productId)
    }
}