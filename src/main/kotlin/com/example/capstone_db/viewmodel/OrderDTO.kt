package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.OrderItem
import java.time.LocalDate

data class OrderDTO(
    val products: MutableList<OrderItem> = mutableListOf(),
    val orderDate: LocalDate?
)