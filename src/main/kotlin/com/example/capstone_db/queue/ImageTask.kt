package com.example.capstone_db.queue

import com.example.capstone_db.model.Image
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class ImageTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: ImageTaskType,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: ImageTaskStatus = ImageTaskStatus.PENDING,

    @ManyToOne
    @JoinColumn(name = "image_id")
    val image: Image,

    @Column(name = "last_attempt_time")
    var lastAttemptTime: LocalDateTime? = null,

    @Column(name = "next_attempt_time")
    var nextAttemptTime: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "last_attempt_error_message")
    var lastAttemptErrorMessage: String? = null
)

enum class ImageTaskStatus {
    PENDING,
    ERROR,
    SUCCESS
}

enum class ImageTaskType {
    WATERMARK,
    DOWNSCALE,
    UPSCALE
}