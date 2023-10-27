package com.example.capstone_db.controller

import com.example.capstone_db.service.OrderService
import com.example.capstone_db.service.UserService
import com.example.capstone_db.viewmodel.OrderViewModel
import com.example.capstone_db.viewmodel.UserViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val orderService: OrderService
) {

    @GetMapping
    fun getUsers(): List<UserViewModel>? {
        return userService.getUsers()
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): UserViewModel? {
        return userService.getUserById(userId)
    }

    @DeleteMapping("/{userId}")
    fun deleteUserById(@PathVariable userId: Long) {
        userService.deleteUserById(userId)
    }

    @GetMapping("/{userId}/orders/delivered")
    fun getDeliveredOrders(@PathVariable userId: Long): ResponseEntity<List<OrderViewModel>?> {
        return try {
            val deliveredOrders = orderService.getDeliveredOrdersForUser(userId)
            ResponseEntity.status(HttpStatus.OK).body(deliveredOrders)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @DeleteMapping("/delete")
    fun deleteLoggedInUser(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        println(username)
        return userService.deleteUserByUsername(username)
    }
}