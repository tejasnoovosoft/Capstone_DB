package com.example.capstone_db.service

import com.example.capstone_db.model.User
import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.security.JwtUtil
import com.example.capstone_db.viewmodel.LoginViewModel
import com.example.capstone_db.viewmodel.RegisterViewModel
import com.example.capstone_db.viewmodel.UserViewModel
import com.example.capstone_db.viewmodel.convertIntoUserViewModel
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
) {
    fun registerUser(registerViewModel: RegisterViewModel): ResponseEntity<Any> {

        lateinit var saveUser: User

        val user = User(
            username = registerViewModel.username,
            email = registerViewModel.email,
            password = registerViewModel.password,
            role = registerViewModel.role
        )
        val encodedPassword = passwordEncoder.encode(user.password)
        val userToSave = user.copy(password = encodedPassword)
        try {
            saveUser = userRepository.save(userToSave)
        } catch (e: Exception) {
            if (userRepository.existsUserByUsername(registerViewModel.username)) {
                throw UserExistsException("Username Already Taken....Please Select Another Username")
            }

            if (userRepository.existsUserByEmail(registerViewModel.email)) {
                throw UserExistsException("Email Already Taken Please Select Another Email")
            }
        }
        return ResponseEntity.ok(convertIntoUserViewModel(saveUser))
    }

    fun loginUser(loginViewModel: LoginViewModel): ResponseEntity<Any> {
        val user = customUserDetailsService.loadUserByUsername(loginViewModel.username)
        return if (comparePassword(loginViewModel.password, user.password)) {
            val jwtToken = jwtUtil.generateToken(user)
            ResponseEntity.ok(jwtToken)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User")
        }
    }

    fun comparePassword(loginPassword: String, userPassword: String): Boolean {
        return passwordEncoder.matches(loginPassword, userPassword)
    }

    fun getLoggedInUser(username: String): UserViewModel? {
        val user = userRepository.findByUsername(username)
        return user?.let { convertIntoUserViewModel(it) }
    }

    @Transactional
    fun resetPassword(userId: Long, newPassword: String): String {
        val encodeNewPassword = passwordEncoder.encode(newPassword)
        userRepository.changePassword(userId, encodeNewPassword)
        return "Password Changed Successfully"
    }

    fun deactivateAccount(userId: Long): ResponseEntity<String> {
        userRepository.deleteById(userId)
        return ResponseEntity.ok("Account Deactivated Successful")
    }
}