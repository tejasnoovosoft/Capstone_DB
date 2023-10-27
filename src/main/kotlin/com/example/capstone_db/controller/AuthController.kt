package com.example.capstone_db.controller

import com.example.capstone_db.service.AuthService
import com.example.capstone_db.viewmodel.LoginViewModel
import com.example.capstone_db.viewmodel.RegisterViewModel
import com.example.capstone_db.viewmodel.UserViewModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody registerViewModel: RegisterViewModel): UserViewModel {
        return authService.registerUser(registerViewModel)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginViewModel: LoginViewModel): ResponseEntity<Any> {
        return authService.loginUser(loginViewModel)
    }

    @GetMapping("/current")
    fun currentLoggedInUser(principal: Principal): UserViewModel? {
        return authService.getLoggedInUser(principal.name)
    }
}