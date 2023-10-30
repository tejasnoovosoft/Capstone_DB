package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.Product

data class ProductViewModel(
    val productId: Long?,
    val productName: String,
    val productPrize: Double,
    val category: String
)

fun convertToProductViewModel(product: Product): ProductViewModel {
    return ProductViewModel(
        productId = product.productId,
        productName = product.productName,
        productPrize = product.productPrize,
        category = product.category
    )
}
