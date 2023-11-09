package com.example.capstone_db.service


import org.springframework.stereotype.Service
import java.io.IOException

@Service
class ImageProcessingService {
    fun manipulateImage(imageBytes: ByteArray, quality: String) {
        try {
            when (quality) {
                "high" -> println("Upscaling Images")
                "low" -> println("Downscaling Images")
                else -> println("Invalid")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw CustomException("Failed to manipulate image")
        }
    }
}