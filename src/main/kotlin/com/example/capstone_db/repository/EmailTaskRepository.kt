package com.example.capstone_db.repository

import com.example.capstone_db.queue.EmailTask
import org.springframework.data.jpa.repository.JpaRepository

interface EmailTaskRepository : JpaRepository<EmailTask, Long> {
}