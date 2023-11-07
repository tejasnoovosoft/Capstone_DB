/*
package com.example.capstone_db.controller

import com.example.capstone_db.model.Product
import com.example.capstone_db.service.FirebaseStorageService
import com.example.capstone_db.service.ImageService
import com.example.capstone_db.service.ProductService
import com.example.capstone_db.viewmodel.ProductViewModel
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/test")
class TestController(
    private val imageService: ImageService,
    private val productService: ProductService,
    private val firebaseStorageService: FirebaseStorageService,
) {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun addProduct(
        @RequestPart productViewModel: ProductViewModel, @RequestPart file: List<MultipartFile>
    ) {
        val bucketName = "capstone-db-6a168.appspot.com"
        val image = firebaseStorageService.uploadFile(file, bucketName)
        val product = Product(
            productName = productViewModel.productName,
            productPrize = productViewModel.productPrize,
            category = productViewModel.category,
            image = image
        )
        productService.addProduct(product)
    }

    @PostMapping("/multiple")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun uploadMultipleFiles(
        @RequestParam("images") files: List<MultipartFile>
    ) {
        println("Total Files ${files.size}")
    }
}
*/
