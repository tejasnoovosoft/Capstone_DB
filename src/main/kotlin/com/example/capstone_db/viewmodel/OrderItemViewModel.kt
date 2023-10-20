package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.OrderItem

data class OrderItemViewModel(
    val product: ProductViewModel,
    val isDelivered: Boolean
)

fun convertToOrderItemViewModel(orderItem : OrderItem?) : OrderItemViewModel {
    return OrderItemViewModel(
        product = convertToProductViewModel(orderItem?.product!!),
        isDelivered = orderItem.isDelivered!!
    )
}