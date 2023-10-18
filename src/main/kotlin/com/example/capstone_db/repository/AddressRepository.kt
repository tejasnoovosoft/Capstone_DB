package com.example.capstone_db.repository

import com.example.capstone_db.model.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address, Long> {
    @Query("SELECT * FROM Address A WHERE a.street = :street AND a.city = :city AND a.pin_code = :pinCode" , nativeQuery = true)
    fun findAddressByDetails(
        @Param("street") street: String,
        @Param("city") city: String,
        @Param("pinCode") pinCode: Long
    ): Address?
}