package com.example.capstone_db.repository

import com.example.capstone_db.queue.ImageTask
import com.example.capstone_db.queue.ImageTaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface ImageTaskRepository : JpaRepository<ImageTask, Long> {
    fun findByNextAttemptTimeLessThanAndStatusOrderByNextAttemptTime(
        dateTime: LocalDateTime,
        status: ImageTaskStatus,
        page: Pageable
    ): List<ImageTask>
}