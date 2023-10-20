package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.Order
import java.time.LocalDate

data class OrderViewModel(
    val products: MutableList<OrderItemViewModel> = mutableListOf(),
    val orderDate: LocalDate?
)

fun convertIntoOrderViewModel(order: Order): OrderViewModel {
    return OrderViewModel(
        products = order.products?.map { convertToOrderItemViewModel(it) }?.toMutableList()!!,
        orderDate = order.orderDate!!
    )
}
