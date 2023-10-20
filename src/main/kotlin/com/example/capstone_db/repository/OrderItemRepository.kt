package com.example.capstone_db.repository

import com.example.capstone_db.model.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem,Long> {
    fun findByProduct_productId(productId: Long): OrderItem?
}