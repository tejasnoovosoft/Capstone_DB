package com.example.capstone_db.service

import com.example.capstone_db.model.Address
import com.example.capstone_db.model.User
import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.viewmodel.UserDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val addressService: AddressService) {
    fun addUser(userDTO: UserDTO): User? {
        val existingAddress =
            addressService.findAddressByDetails(userDTO.address.street, userDTO.address.city, userDTO.address.pinCode)

        val address = existingAddress ?: Address(
            street = userDTO.address.street,
            city = userDTO.address.city,
            pinCode = userDTO.address.pinCode
        )

        val user = User(
            username = userDTO.username,
            email = userDTO.email,
            address = address
        )
        return userRepository.save(user)
    }

    fun isUserExists(email:String) : User? {
        return userRepository.findByEmail(email)
    }

    fun getUsers(): List<User>? {
        return userRepository.findAll()
    }

    fun getUserById(userId: Long): User? {
        return userRepository.findByIdOrNull(userId)
    }

    fun deleteUserById(userId: Long) {
        userRepository.deleteById(userId)
    }
}