package com.example.capstone_db.repository

import com.example.capstone_db.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
}