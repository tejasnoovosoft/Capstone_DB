package com.example.capstone_db.viewmodel

import com.example.capstone_db.model.Address

data class AddressViewModel(
    val street: String,
    val city: String,
    val pinCode: Long
)

fun convertIntoAddressViewMode(address: Address): AddressViewModel {
    return AddressViewModel(
        street = address.street,
        city = address.city,
        pinCode = address.pinCode
    )
}