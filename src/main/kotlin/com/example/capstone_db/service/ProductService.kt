package com.example.capstone_db.service

import com.example.capstone_db.model.Product
import com.example.capstone_db.repository.ProductRepository
import com.example.capstone_db.viewmodel.ProductOutputViewModel
import com.example.capstone_db.viewmodel.ProductViewModel
import com.example.capstone_db.viewmodel.convertToProductOutputViewModel
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val imageProcessingService: ImageProcessingService
) {

    /*    @Async("threadPoolTaskExecutor")
        fun addProducts(productItems: List<ProductViewModel>) {
            for (productItem in productItems) {
                CompletableFuture.runAsync({
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
        }*/

    fun addProduct(productItem: ProductViewModel) {
        imageProcessingService.addingTaskInQueue(productItem.images)
        val imageUrls = imageProcessingService.startImageProcessing(2).join()

        val product =
            Product(
                productName = productItem.productName,
                productPrize = productItem.productPrize,
                category = productItem.category,
                image = imageUrls
            )
        productRepository.save(product)
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