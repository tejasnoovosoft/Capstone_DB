package com.example.capstone_db.repository

import com.example.capstone_db.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByUser_userId(userId: Long): List<Order>?
    fun findByUserUserIdAndOrderDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Order>?

    fun findByUserUserIdAndProductsIsDelivered(userId: Long, isDelivered: Boolean): List<Order>?

}