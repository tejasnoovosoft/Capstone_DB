package com.example.capstone_db.service

import com.example.capstone_db.model.Address
import com.example.capstone_db.repository.AddressRepository
import org.springframework.stereotype.Service

@Service
class AddressService(private val addressRepository: AddressRepository) {
    fun findAddressByDetails(street: String, city: String, pinCode: Long): Address? {
        return addressRepository.findAddressByDetails(street,city,pinCode)
    }
}