package com.example.capstone_db.queue

import com.example.capstone_db.model.Status
import jakarta.persistence.*

@Entity
data class EmailTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(name = "email_id")
    val emailId: String,

    @Column(name = "content")
    val content: String,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    ) : Task()
