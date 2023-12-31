package com.example.capstone_db.service

import com.example.capstone_db.model.Order
import com.example.capstone_db.model.OrderItem
import com.example.capstone_db.repository.OrderItemRepository
import com.example.capstone_db.repository.OrderRepository
import com.example.capstone_db.repository.ProductRepository
import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.viewmodel.OrderViewModel
import com.example.capstone_db.viewmodel.convertIntoOrderViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val orderItemRepository: OrderItemRepository, private val productRepository: ProductRepository
) {
    fun addOrder(userId: Long, order: Order) {
        val user = userRepository.findById(userId).get()
        order.user = user
        val savedOrder = orderRepository.save(order)
        savedOrder.products?.forEach {
            it.product?.let { product ->
                val existedProduct = productRepository.findByproductName(product.productName)
                if (existedProduct != null) {
                    val orderItem = OrderItem(
                        product = existedProduct,
                        isDelivered = false,
                        order = savedOrder
                    )
                    orderItemRepository.save(orderItem)
                } else {
                    orderRepository.deleteById(savedOrder.orderId!!)
                    throw ProductNotFoundException("Product with name ${product.productName} not found")
                }
            }
        }
    }

    fun getOrdersByUserId(userId: Long): List<OrderViewModel>? {
        val orders = orderRepository.findByUserUserId(userId)
        return orders?.map { convertIntoOrderViewModel(it) }
    }

    fun deleteOrderByOrderId(userId: Long, orderId: Long): ResponseEntity<String> {
        val userExists = userRepository.existsById(userId)
        return if (userExists) {
            val order = orderRepository.findById(orderId)
            if (order.isPresent) {
                orderRepository.deleteById(orderId)
                ResponseEntity.status(HttpStatus.OK).body("Order Deleted Successfully")
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found")
            }
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User")
        }
    }

    fun deleteProductFromOrderList(userId: Long, orderId: Long, productId: Long): ResponseEntity<String> {
        val user = userRepository.existsById(userId)
        return if (user) {
            orderRepository.findById(orderId).get()
            val orderItem = orderItemRepository.findByProductProductId(productId)
            orderItem?.let {
                orderItemRepository.delete(it)
            } ?: throw ProductNotFoundException("Product with ID $productId not found")
            ResponseEntity.status(HttpStatus.OK).body("Product Deleted Successfully")
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User")
        }
    }

    fun getAllOrders(): List<OrderViewModel>? {
        val orders = orderRepository.findAll()
        return orders.map { convertIntoOrderViewModel(it) }
    }

    fun getOrdersBetweenDates(userId: Long, startDate: LocalDate, endDate: LocalDate): List<OrderViewModel>? {
        val orders = orderRepository.findByUserUserIdAndOrderDateBetween(userId, startDate, endDate)
        return orders?.map { convertIntoOrderViewModel(it) }
    }

    fun getDeliveredOrdersForUser(userId: Long): List<OrderViewModel>? {
        val orders = orderRepository.findByUserUserIdAndProductsIsDelivered(userId, true)
        return orders?.map { convertIntoOrderViewModel(it) }
    }

}