package com.example.capstone_db.controller

import com.example.capstone_db.service.UserService
import com.example.capstone_db.viewmodel.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}