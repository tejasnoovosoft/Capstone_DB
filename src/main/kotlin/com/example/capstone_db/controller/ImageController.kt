package com.example.capstone_db.controller

import com.example.capstone_db.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ImageController(
    private val productService: ProductService,
) {

    @GetMapping("/{productId}/images")
    fun getProductImages(
        @PathVariable productId: Long,
        @RequestParam quality: String
    ): List<String> {
        when (quality) {
            "high" -> println("Upscaling Images")
            "low" -> println("Downscaling Images")
            else -> println("Invalid")
        }
        return productService.getProductImagesById(productId)!!
    }
}