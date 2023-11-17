package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import com.example.capstone_db.queue.ImageTask
import com.example.capstone_db.queue.ImageTaskType
import com.example.capstone_db.repository.ImageRepository
import com.example.capstone_db.repository.ImageTaskRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class ImageService(
    @Value("\${imagedatabase}")
    private val baseUrl: String,
    @Value("\${imageserver}")
    private val uploadPath: String,
    private val imageRepository: ImageRepository,
    private val imageTaskRepository: ImageTaskRepository
) {

    val folderPath = "C:/Users/TejasEkhande/Desktop/Capstone_Images/"
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

    fun uploadImageToFileSystem(imageData: ByteArray, fileName: String, folderName: String): String {
        println("Saving: $fileName in Thread ${Thread.currentThread().name}")
        val newFolderPath = folderPath + folderName
        File(newFolderPath).mkdirs()
        val filePath = "${newFolderPath}/${fileName}"
        Files.write(Paths.get(filePath), imageData)
        return filePath
    }

    @Async
    fun uploadImage(imageData: ByteArray): CompletableFuture<Image> {
        val folderName = UUID.randomUUID().toString().replace("-", "")
        val fileName = "$folderName.png"
        val filePath = uploadImageToFileSystem(imageData, fileName, folderName)
        val image = imageRepository.save(Image(url = filePath))
        imageTaskRepository.save(ImageTask(type = ImageTaskType.WATERMARK, image = image))
        imageTaskRepository.save(ImageTask(type = ImageTaskType.UPSCALE, image = image))
        imageTaskRepository.save(ImageTask(type = ImageTaskType.DOWNSCALE, image = image))
        return CompletableFuture.completedFuture(image)
    }
}

fun determineContentType(imagePath: String): String {
    val extension = imagePath.substringAfterLast('.')
    return when (extension.lowercase(Locale.getDefault())) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        else -> "application/octet-stream"
    }
}