package com.example.capstone_db.repository

import com.example.capstone_db.queue.ImageTask
import org.springframework.data.jpa.repository.JpaRepository

interface ImageTaskRepository : JpaRepository<ImageTask, Long> {

}