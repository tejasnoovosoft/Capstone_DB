package com.example.capstone_db.controller

import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.service.OrderService
import com.example.capstone_db.service.UserService
import com.example.capstone_db.viewmodel.AddressViewModel
import com.example.capstone_db.viewmodel.OrderViewModel
import com.example.capstone_db.viewmodel.UserDTO
import com.example.capstone_db.viewmodel.UserViewModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val orderService: OrderService
) {

    @PostMapping
    fun addUser(@RequestBody userDTO: UserDTO): ResponseEntity<String> {
        val email = userDTO.email
        return try {
            val existingUser = userService.getUserByEmail(email)
            if (existingUser != null) {
                ResponseEntity.status(HttpStatus.CONFLICT).body("User Already Registered with $email")
            } else {
                userService.addUser(userDTO)
                ResponseEntity.status(HttpStatus.OK).body("User Created Successfully")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing your request")
        }
    }

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

    @PutMapping("/{userId}/address")
    fun updateUserAddress(
        @PathVariable userId: Long, @RequestBody addressViewModel: AddressViewModel
    ): ResponseEntity<String> {
        val existingUser = userRepository.findById(userId)
        return if (existingUser.isPresent) {
            userService.updateAddress(addressViewModel, userId)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found")
        }
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
}