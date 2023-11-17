package com.example.capstone_db.queue

import com.example.capstone_db.model.Image
import com.example.capstone_db.model.Status
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class ImageTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override  val id: Long = 0,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: ImageTaskType,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    @ManyToOne
    @JoinColumn(name = "image_id")
    val image: Image,

    ) : Task()
enum class ImageTaskType {
    WATERMARK,
    DOWNSCALE,
    UPSCALE
}