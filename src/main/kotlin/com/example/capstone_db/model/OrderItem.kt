package com.example.capstone_db.model

import jakarta.persistence.*

@Entity
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product? = null,

    @Column(name = "is_delivered")
    val isDelivered: Boolean? = null,

    @ManyToOne
    @JoinColumn(name = "order_id")
    val order: Orders
)
