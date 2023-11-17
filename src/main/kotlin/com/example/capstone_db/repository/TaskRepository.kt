package com.example.capstone_db.repository
import com.example.capstone_db.model.Status
import com.example.capstone_db.queue.Task
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TaskRepository : JpaRepository<Task, Long> {

    fun findByNextAttemptTimeLessThanAndTaskStatusOrderByNextAttemptTime(
        dateTime: LocalDateTime,
        taskStatus: Status,
        page: Pageable
    ): List<Task>
}