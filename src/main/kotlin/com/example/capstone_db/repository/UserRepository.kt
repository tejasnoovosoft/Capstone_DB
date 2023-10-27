package com.example.capstone_db.repository

import com.example.capstone_db.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByUsername(username: String): User?

    fun deleteUserByUsername(username: String): Int

}