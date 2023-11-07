package com.example.capstone_db.controller

import com.example.capstone_db.service.CustomException
import com.example.capstone_db.service.ProductService
import com.example.capstone_db.viewmodel.ProductOutputViewModel
import com.example.capstone_db.viewmodel.ProductViewModel
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun addProducts(@RequestPart productItems: List<ProductViewModel>): String {
        try {
            val products = CompletableFuture.runAsync {
                productService.addProducts(productItems)
            }
            products.join()
            return "Products Added Successfully"
        } catch (e: Exception) {
            throw CustomException("An error occurred while adding products: " + e.message)
        }
    }


    @GetMapping
    fun getProducts(): List<ProductOutputViewModel>? {
        return productService.getProducts()
    }

    @GetMapping("/{productId}")
    fun findProductById(@PathVariable productId: Long): ProductOutputViewModel? {
        return productService.findProductById(productId)
    }

    @GetMapping("/category/{category}")
    fun findProductsByCategory(@PathVariable category: String): List<ProductOutputViewModel>? {
        return productService.findProductsByCategory(category)
    }

    @GetMapping("/filter")
    fun findProductsBetweenPrice(
        @RequestParam("minPrice") minPrice: Double,
        @RequestParam("maxPrice") maxPrice: Double
    ): List<ProductOutputViewModel>? {
        return productService.findProductsBetweenPrice(minPrice, maxPrice)
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteProductById(@PathVariable productId: Long) {
        return productService.deleteProductById(productId)
    }

    @PutMapping("{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateProductById(@PathVariable productId: Long, @RequestBody productViewModel: ProductOutputViewModel) {
        return productService.updateProductById(productId, productViewModel)
    }

    @GetMapping("/product?search={productName}")
    fun getProductByName(@PathVariable productName: String): ResponseEntity<ProductOutputViewModel>? {
        val product = productService.getProductByName(productName)
        return ResponseEntity.ok(product)
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam product: String): ResponseEntity<List<ProductOutputViewModel>> {
        return productService.searchProducts(product)
    }
}