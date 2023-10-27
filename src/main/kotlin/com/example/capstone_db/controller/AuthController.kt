package com.example.capstone_db.controller

import com.example.capstone_db.service.AuthService
import com.example.capstone_db.viewmodel.LoginViewModel
import com.example.capstone_db.viewmodel.RegisterViewModel
import com.example.capstone_db.viewmodel.UserViewModel
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
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

    @GetMapping("/logout")
    fun logout(): String {
        SecurityContextHolder.clearContext()
        return "redirect:/login"
    }

    @GetMapping("/current/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getAdminDetails(principal: Principal): UserViewModel? {
        return authService.getLoggedInUser(principal.name)
    }

    @GetMapping("/current/user")
    @PreAuthorize("hasAuthority('USER')")
    fun getUserDetails(principal: Principal): UserViewModel? {
        return authService.getLoggedInUser(principal.name)
    }
}