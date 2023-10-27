package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.User

data class UserViewModel(
    val userId: Long,
    val userName: String,
    val email: String,
    val role: String
)

fun convertIntoUserViewModel(user: User): UserViewModel {
    return UserViewModel(
        userId = user.userId!!,
        userName = user.username,
        email = user.email,
        role = user.role
    )
}