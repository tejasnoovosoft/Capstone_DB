package com.example.capstone_db.service

import com.example.capstone_db.model.OrderItem
import com.example.capstone_db.repository.OrderItemRepository
import org.springframework.stereotype.Service

@Service
class OrderItemService(private val orderItemRepository: OrderItemRepository) {
    fun saveOrderItem(orderItem: OrderItem): OrderItem {
        return orderItemRepository.save(orderItem)
    }
}