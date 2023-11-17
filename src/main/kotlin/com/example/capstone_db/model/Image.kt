package com.example.capstone_db.model

import jakarta.persistence.*

@Entity
@Table(name = "images")
data class Image(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "url", columnDefinition = "TEXT")
    val url: String
)