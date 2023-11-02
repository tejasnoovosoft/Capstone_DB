package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.Product

data class ProductViewModel(
    val productName: String,
    val productPrize: Double,
    val category: String
)
data class ProductOutputViewModel(
    val productId: Long?,
    val productName: String,
    val productPrize: Double,
    val category: String,
    val imageUrl: List<String>
)

fun convertToProductOutputViewModel(product: Product): ProductOutputViewModel {
    return ProductOutputViewModel(
        productId = product.productId,
        productName = product.productName,
        productPrize = product.productPrize,
        category = product.category,
        imageUrl = product.image.map { it.url }
    )
}
