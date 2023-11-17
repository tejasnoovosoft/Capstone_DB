package com.example.capstone_db.repository

import com.example.capstone_db.model.Status
import com.example.capstone_db.queue.ImageTask
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface ImageTaskRepository : JpaRepository<ImageTask, Long> {
    fun findByNextAttemptTimeLessThanAndStatusOrderByNextAttemptTime(
        dateTime: LocalDateTime,
        status: Status,
        page: Pageable
    ): List<ImageTask>
}