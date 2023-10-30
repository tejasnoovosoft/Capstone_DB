package com.example.capstone_db.repository

import com.example.capstone_db.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByUsername(username: String): User?

    fun deleteUserByUsername(username: String): Int

    fun existsUserByUsername(username: String): Boolean

    fun existsUserByEmail(email: String): Boolean

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userId = :userId")
    fun changePassword(userId: Long, newPassword: String)
}