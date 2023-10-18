package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.Address

data class UserDTO(
    val username: String,
    val email: String,
    val address: Address
)
