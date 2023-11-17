package com.example.capstone_db.controller

import com.example.capstone_db.service.EmailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class EmailController(private val emailService: EmailService) {

    @GetMapping("/{userId}/email")
    fun userInfo(@PathVariable userId: Long) {
        emailService.userInfo(userId)
    }
}