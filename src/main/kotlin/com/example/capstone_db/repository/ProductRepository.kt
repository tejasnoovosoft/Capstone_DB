package com.example.capstone_db.repository

import com.example.capstone_db.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByproductName(name: String): Product?
    fun findByCategory(category: String): List<Product>?

    @Query(value = "SELECT * FROM products WHERE price between :minPrice and :maxPrice", nativeQuery = true)
    fun findProductsBetweenPrice(minPrice: Double, maxPrice: Double): List<Product>?

    @Modifying
    @Query("UPDATE Product p SET p.productName = :productName, p.productPrize = :productPrize, p.category = :category WHERE p.productId = :productId")
    fun updateProduct(
        @Param("productId") productId: Long,
        @Param("productName") productName: String,
        @Param("productPrize") productPrize: Double,
        @Param("category") category: String
    ): Int

    @Modifying
    @Query(
        "INSERT INTO products (product_id, product_name, price, category) VALUES (:productId, :productName, :productPrize, :category)",
        nativeQuery = true
    )
    fun saveProductWithId(
        @Param("productId") productId: Long,
        @Param("productName") productName: String,
        @Param("productPrize") productPrize: Double,
        @Param("category") category: String
    ): Int
}