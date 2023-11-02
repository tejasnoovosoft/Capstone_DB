package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageService(
    @Value("\${imagedatabase}")
    private val baseUrl: String,
    @Value("\${imageserver}")
    private val uploadPath: String // Specify the upload directory in your application.properties
) {
    fun convertToImage(file: MultipartFile): Image {
        val uuid = UUID.randomUUID().toString()
        val extension = file.originalFilename?.substringAfterLast('.')
        val fileName = "$uuid.$extension"
        val filePath = "$uploadPath${File.separator}$fileName"

        try {
            Files.copy(file.inputStream, File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING)
            val imageUrl = "$baseUrl/$fileName"
            return Image(url = imageUrl)
        } catch (e: Exception) {
            throw RuntimeException("Failed to save image: ${e.message}")
        }
    }
}

fun convertToByteArray(image: Image): ByteArray {
    val imagePath = image.url
    return Files.readAllBytes(File(imagePath).toPath())

}

fun determineContentType(imagePath: Path): String {
    val fileName = imagePath.fileName.toString()
    val extension = fileName.substringAfterLast('.')
    return when (extension.lowercase(Locale.getDefault())) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        else -> "application/octet-stream"
    }
}