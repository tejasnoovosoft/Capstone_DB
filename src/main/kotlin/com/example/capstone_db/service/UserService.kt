package com.example.capstone_db.service

import com.example.capstone_db.model.Address
import com.example.capstone_db.model.User
import com.example.capstone_db.repository.AddressRepository
import com.example.capstone_db.repository.UserRepository
import com.example.capstone_db.viewmodel.AddressViewModel
import com.example.capstone_db.viewmodel.UserDTO
import com.example.capstone_db.viewmodel.UserViewModel
import com.example.capstone_db.viewmodel.convertIntoUserViewModel
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressService: AddressService,
    private val addressRepository: AddressRepository
) {
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

    fun getUserByEmail(email:String) : User? {
        return userRepository.findByEmail(email)
    }

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

    fun updateAddress(addressViewModel: AddressViewModel, userId: Long): ResponseEntity<String> {
        val address = Address(
            street = addressViewModel.street,
            city = addressViewModel.city,
            pinCode = addressViewModel.pinCode
        )

        val existingAddress = addressRepository.findAddressByDetails(address.street, address.city, address.pinCode)

        if (existingAddress == null) {
            addressRepository.save(address)
        }

        val updatedRows = userRepository.updateUserAddress(userId, address)

        return if (updatedRows > 0) {
            ResponseEntity.status(HttpStatus.OK).body("User Address Updated Successfully")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user address")
        }
    }
}