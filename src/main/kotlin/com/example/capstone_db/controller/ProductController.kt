package com.example.capstone_db.controller

import com.example.capstone_db.model.Product
import com.example.capstone_db.service.ProductService
import com.example.capstone_db.viewmodel.ProductViewModel
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun addProduct(@RequestBody productViewModel: ProductViewModel): Product? {
        val product = Product(
            productName = productViewModel.productName,
            productPrize = productViewModel.productPrize,
            category = productViewModel.category
        )
        return productService.addProduct(product)
    }

    @GetMapping
    fun getProducts(): List<ProductViewModel>? {
        return productService.getProducts()
    }

    @GetMapping("/{productId}")
    fun findProductById(@PathVariable productId: Long): ProductViewModel? {
        return productService.findProductById(productId)
    }

    @GetMapping("/category/{category}")
    fun findProductsByCategory(@PathVariable category: String): List<ProductViewModel>? {
        return productService.findProductsByCategory(category)
    }

    @GetMapping("/filter")
    fun findProductsBetweenPrice(
        @RequestParam("minPrice") minPrice: Double,
        @RequestParam("maxPrice") maxPrice: Double
    ): List<ProductViewModel>? {
        return productService.findProductsBetweenPrice(minPrice, maxPrice)
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteProductById(@PathVariable productId: Long) {
        return productService.deleteProductById(productId)
    }

    @PutMapping("{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateProductById(@PathVariable productId: Long, @RequestBody productViewModel: ProductViewModel) {
        return productService.updateProductById(productId, productViewModel)
    }

    @GetMapping("/product?search={productName}")
    fun getProductByName(@PathVariable productName: String): ResponseEntity<ProductViewModel>? {
        val product = productService.getProductByName(productName)
        return ResponseEntity.ok(product)
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam product: String): ResponseEntity<List<ProductViewModel>> {
        return productService.searchProducts(product)
    }
}