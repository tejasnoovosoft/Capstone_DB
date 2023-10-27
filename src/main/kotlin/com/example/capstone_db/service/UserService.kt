package com.example.capstone_db.service

import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.viewmodel.UserViewModel
import com.example.capstone_db.viewmodel.convertIntoUserViewModel
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {


    fun getUsers(): List<UserViewModel> {
        val users = userRepository.findAll()
        return users.map {
            convertIntoUserViewModel(it)
        }
    }

    fun getUserById(userId: Long): UserViewModel? {
        val user = userRepository.findByIdOrNull(userId)
        return user?.let { convertIntoUserViewModel(it) }
    }

    fun deleteUserById(userId: Long) {
        userRepository.deleteById(userId)
    }

    @Transactional
    fun deleteUserByUsername(username: String): String {
        val rowsAffected = userRepository.deleteUserByUsername(username)
        return if (rowsAffected > 0) {
            "User Deleted Successfully"
        } else {
            "Invalid Operation"
        }
    }

}