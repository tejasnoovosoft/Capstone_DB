package com.example.capstone_db.controller

import com.example.capstone_db.model.Order
import com.example.capstone_db.service.OrderService
import com.example.capstone_db.service.UserNotFoundException
import com.example.capstone_db.viewmodel.OrderDTO
import com.example.capstone_db.viewmodel.OrderViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/users")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/{userId}/orders")
    @ExceptionHandler(UserNotFoundException::class)
    fun orderByUser(@PathVariable userId: Long, @RequestBody orderDTO: OrderDTO): ResponseEntity<String> {
        return try {
            val order = Order(
                products = orderDTO.products, orderDate = orderDTO.orderDate
            )
            orderService.addOrder(userId, order)
            ResponseEntity.status(HttpStatus.OK).body("Order Placed Successfully")
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found")
        }
    }

    @GetMapping("/{userId}/orders")
    fun getOrdersByUserId(@PathVariable userId: Long): List<OrderViewModel>? {
        return orderService.getOrdersByUserId(userId)
    }


    @DeleteMapping("/{userId}/orders/{orderId}")
    fun deleteOrderByOrderId(@PathVariable userId: Long, @PathVariable orderId: Long): ResponseEntity<String> {
        return orderService.deleteOrderByOrderId(userId, orderId)
    }

    @DeleteMapping("/{userId}/orders/{orderId}/{productId}")
    fun deleteProduct(
        @PathVariable userId: Long,
        @PathVariable orderId: Long,
        @PathVariable productId: Long
    ): ResponseEntity<String> {
        return orderService.deleteProductFromOrderList(userId, orderId, productId)
    }

    @GetMapping("/orders")
    fun getAllOrders(): List<OrderViewModel>? {
        return orderService.getAllOrders()
    }

    @GetMapping("/{userId}/orders/date")
    fun getOrdersBetweenDates(
        @PathVariable userId: Long,
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): List<OrderViewModel>? {
        return orderService.getOrdersBetweenDates(userId, startDate, endDate)
    }

}