package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.OrderItem

data class OrderItemViewModel(
    val product: ProductOutputViewModel,
    val isDelivered: Boolean
)

fun convertToOrderItemViewModel(orderItem : OrderItem?) : OrderItemViewModel {
    return OrderItemViewModel(
        product = convertToProductOutputViewModel(orderItem?.product!!),
        isDelivered = orderItem.isDelivered!!
    )
}