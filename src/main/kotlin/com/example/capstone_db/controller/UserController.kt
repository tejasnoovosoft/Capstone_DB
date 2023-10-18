package com.example.capstone_db.controller

import com.example.capstone_db.model.User
import com.example.capstone_db.service.UserService
import com.example.capstone_db.viewmodel.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun addUser(@RequestBody userDTO: UserDTO): ResponseEntity<String> {
        val isUserExists = userService.isUserExists(userDTO.email)
        return if (isUserExists == null) {
            userService.addUser(userDTO)
            ResponseEntity.status(HttpStatus.OK).body("User Created Successfully")
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).body("User Already Registered with ${userDTO.email}")
        }
    }

    @GetMapping
    fun getUsers(): List<User>? {
        return userService.getUsers()
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): User? {
        return userService.getUserById(userId)
    }

    @DeleteMapping("/{userId}")
    fun deleteUserById(@PathVariable userId: Long) {
        userService.deleteUserById(userId)
    }
}