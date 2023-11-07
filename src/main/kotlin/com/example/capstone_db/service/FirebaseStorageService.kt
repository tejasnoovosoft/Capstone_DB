package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import com.google.firebase.cloud.StorageClient
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID.randomUUID
import java.util.concurrent.CompletableFuture

@Service
class FirebaseStorageService(
    private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {
    @Async("threadPoolTaskExecutor")
    fun uploadFile(
        file: List<MultipartFile>
    ): CompletableFuture<List<Image>> {
        val futures = (file.map { multipartFile ->
            CompletableFuture.supplyAsync({
                println("InSide : ${Thread.currentThread().name}")
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
    }

}
