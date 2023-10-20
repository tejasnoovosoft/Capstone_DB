package com.example.capstone_db.repository

import com.example.capstone_db.model.Address
import com.example.capstone_db.model.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmail(email:String) : User?

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.address = :address WHERE u.userId = :userId")
    fun updateUserAddress(@Param("userId") userId: Long, @Param("address") address: Address): Int
}