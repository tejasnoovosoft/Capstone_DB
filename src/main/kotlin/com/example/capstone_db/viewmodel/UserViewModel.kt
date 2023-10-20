package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.User

data class UserViewModel(
    val userId: Long,
    val userNumber: String,
    val email: String,
    val address: AddressViewModel
)

fun convertIntoUserViewModel(user: User): UserViewModel {
    return UserViewModel(
        userId = user.userId!!,
        userNumber = user.username,
        email = user.email,
        address = convertIntoAddressViewMode(user.address)
    )
}