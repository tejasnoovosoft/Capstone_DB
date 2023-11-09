package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import com.google.firebase.cloud.StorageClient
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
class FirebaseStorageService{
   /* @Async("threadPoolTaskExecutor")
    fun uploadFile(
        file: List<MultipartFile>
    ): CompletableFuture<List<Image>> {
        val futures = (file.map { multipartFile ->
            CompletableFuture.supplyAsync({
                val uuid = randomUUID().toString()
                val extension = multipartFile.originalFilename?.substringAfterLast('.')
                val fileName = "$uuid.$extension"
                val bucket = StorageClient.getInstance().bucket()
                bucket.create(fileName, multipartFile.bytes, multipartFile.contentType)
                val url = "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${fileName}?alt=media"
                Image(url = url)
            }, threadPoolTaskExecutor)
        })

        val allFutures = CompletableFuture.allOf(*futures.toTypedArray())

        return allFutures.thenApply {
            futures.map { it.join() }
        }
    }*/

    fun uploadImage(base64EncodedImage: String, imageName: String): Image {
        val imageBytes = Base64.getDecoder().decode(base64EncodedImage)
        val uuid = randomUUID().toString()
        val extension = imageName.substringAfterLast('.')
        val fileName = "$uuid.$extension"
        val bucket = StorageClient.getInstance().bucket()
        val contentType = determineContentType(imageName)
        bucket.create(fileName, imageBytes, contentType)
        val url = "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${fileName}?alt=media"
        return Image(url = url)
    }
}
