package com.example.capstone_db.controller

import com.example.capstone_db.model.Product
import com.example.capstone_db.service.ProductService
import com.example.capstone_db.viewmodel.ProductDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {
    @PostMapping
    fun addProduct(@RequestBody productDTO: ProductDTO): Product? {
        val product = Product(
            productName = productDTO.productName,
            productPrize = productDTO.productPrize,
            category = productDTO.category
        )
        return productService.addProduct(product)
    }

    @GetMapping
    fun getProducts(): List<Product>? {
        return productService.getProducts()
    }

    @GetMapping("/{productId}")
    fun findProductById(@PathVariable productId: Long): Product? {
        return productService.findProductById(productId)
    }

    @GetMapping("/category/{category}")
    fun findProductsByCategory(@PathVariable category: String): List<Product>? {
        return productService.findProductsByCategory(category)
    }

    @GetMapping("/filter")
    fun findProductsBetweenPrice(
        @RequestParam("minPrice") minPrice: Double,
        @RequestParam("maxPrice") maxPrice: Double
    ): List<Product>? {
        return productService.findProductsBetweenPrice(minPrice, maxPrice)
    }

    @DeleteMapping("{productId}")
    fun deleteProductById(@PathVariable productId: Long) {
        return productService.deleteProductById(productId)
    }

    @PutMapping("{productId}")
    fun updateProductById(@PathVariable productId: Long, @RequestBody productDTO: ProductDTO) {
        return productService.updateProductById(productId, productDTO)
    }
}