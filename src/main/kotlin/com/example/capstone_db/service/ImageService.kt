package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.StorageOptions
import com.google.firebase.FirebaseApp
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageService(
    @Value("\${imagedatabase}")
    private val baseUrl: String,
    @Value("\${imageserver}")
    private val uploadPath: String
) {
    fun convertToImage(file: List<MultipartFile>): List<Image> {
        return file.map {
            val uuid = UUID.randomUUID().toString()
            val extension = it.originalFilename?.substringAfterLast('.')
            val fileName = "$uuid.$extension"
            val filePath = "$uploadPath${File.separator}$fileName"

            try {
                Files.copy(it.inputStream, File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING)
                val imageUrl = "$baseUrl/$fileName"
                Image(url = imageUrl)
            } catch (e: Exception) {
                throw RuntimeException("Failed to save image: ${e.message}")
            }
        }
    }

    fun convertToByteArray(image: List<Image>): List<ByteArray> {
        return image.map { Files.readAllBytes(File(it.url).toPath()) }

    }

    fun getImageBytesFromFirebase(imageUrl: String) {

    }
}



fun determineContentType(imagePath: String): String {
//    val fileName = imagePath.fileName.toString()
    val extension = imagePath.substringAfterLast('.')
    return when (extension.lowercase(Locale.getDefault())) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        else -> "application/octet-stream"
    }
}