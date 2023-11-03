package com.example.capstone_db.service

import com.example.capstone_db.model.Image
import com.google.firebase.cloud.StorageClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID.randomUUID

@Service
class FirebaseStorageService {

    fun uploadFile(file: List<MultipartFile>, bucketName: String): List<Image> {
        val bucket = StorageClient.getInstance().bucket(bucketName)
        return file.map {
            val uuid = randomUUID().toString()
            val extension = it.originalFilename?.substringAfterLast('.')
            val fileName = "$uuid.$extension"

            bucket.create(fileName, it.bytes, it.contentType)
            val url = "https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${fileName}?alt=media"
            Image(url = url)
        }
    }
}
