package com.example.capstone_db.service

import com.example.capstone_db.queue.EmailTask
import com.example.capstone_db.repository.EmailTaskRepository
import com.example.capstone_db.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class EmailService(private val userRepository: UserRepository, private val emailTaskRepository: EmailTaskRepository) {
    fun userInfo(userId: Long) {
        val user = userRepository.findUserByUserId(userId)!!
        emailTaskRepository.save(EmailTask(emailId = user.email, content = "${user.role} : ${user.username}"))
    }
}