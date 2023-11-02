package com.example.capstone_db.model

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val productId: Long? = null,
    @Column(name = "product_name", length = 25, unique = true)
    val productName: String,
    @Column(name = "price")
    val productPrize: Double,
    @Column(name = "category", length = 25)
    val category: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val image: Image
)