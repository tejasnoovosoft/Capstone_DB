package com.example.capstone_db.queue

import com.example.capstone_db.model.Status
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "task_type", discriminatorType = DiscriminatorType.STRING)
open class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null

    @Column(name = "task_type" , insertable = false , updatable = false)
    open val taskType: String = ""

    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    var taskStatus: Status = Status.PENDING

    @Column(name = "last_attempt_time")
    var lastAttemptTime: LocalDateTime? = null

    @Column(name = "next_attempt_time")
    var nextAttemptTime: LocalDateTime? = LocalDateTime.now()

    @Column(name = "last_attempt_error_message")
    var lastAttemptErrorMessage: String? = null
}
