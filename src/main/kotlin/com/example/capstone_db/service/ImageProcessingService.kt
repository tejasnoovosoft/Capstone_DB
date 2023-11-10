package com.example.capstone_db.service


import com.example.capstone_db.model.Image
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue

@Service
class ImageProcessingService(
    private val firebaseStorageService: FirebaseStorageService,
    @Qualifier("threadPoolTaskExecutor") private val threadPoolTaskExecutor: ThreadPoolTaskExecutor
) {

    private val logger: Logger = LoggerFactory.getLogger(ImageProcessingService::class.java)
    private val imageProcessingQueue: BlockingQueue<String> = LinkedBlockingQueue()

    fun addingTaskInQueue(imageUrls: List<String>) {
        imageProcessingQueue.addAll(imageUrls)
    }

    /*@Async("threadPoolTaskExecutor")
    fun startImageProcessing(): CompletableFuture<List<Image>> {
        val imageUrlsFutures = mutableListOf<CompletableFuture<Image>>()
        while (!imageProcessingQueue.isEmpty()) {
            val imageUrl = imageProcessingQueue.take()
            val imageFuture = CompletableFuture.supplyAsync {
                try {
                    processImage(imageUrl)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
            imageUrlsFutures.add(imageFuture)
        }

        val allOf = CompletableFuture.allOf(*imageUrlsFutures.toTypedArray())

        return allOf.thenApply {
            imageUrlsFutures.map { it.join() }
        }
    }*/

    @Async("threadPoolTaskExecutor")
    fun startImageProcessing(batchSize: Int): CompletableFuture<List<Image>> {
        val imageFutures = mutableListOf<CompletableFuture<Image>>()

        while (!imageProcessingQueue.isEmpty()) {
            val imageUrls = mutableListOf<String>()
            repeat(batchSize) {
                imageProcessingQueue.poll()?.let { imageUrl ->
                    imageUrls.add(imageUrl)
                }
            }
            val batchFutures = imageUrls.map { imageUrl ->
                CompletableFuture.supplyAsync({
                    processImage(imageUrl)
                }, threadPoolTaskExecutor)
            }

            imageFutures.addAll(batchFutures)
        }

        val allOf = CompletableFuture.allOf(*imageFutures.toTypedArray())

        return allOf.thenApply {
            imageFutures.map { it.join() }
        }
    }


    private fun processImage(imageUrl: String): Image {
        try {
            upscaleImage()
            downscaleImage()
            addWatermark()
            return uploadImage(imageUrl)
        } catch (e: Exception) {
            logger.error("Error processing image $imageUrl", e)
            throw e
        }
    }

    private fun upscaleImage() {
        logger.info("Upscaling Image Start...! ${Thread.currentThread().name}")
        Thread.sleep(5000)
        logger.info("Upscaling Image Completed...! ${Thread.currentThread().name}")
    }

    private fun downscaleImage() {
        logger.info("Downscale Image Start...! ${Thread.currentThread().name}")
        Thread.sleep(5000)
        logger.info("Downscale Image Completed...! ${Thread.currentThread().name}")
    }

    private fun addWatermark() {
        logger.info("Adding Watermark on Image Started...! ${Thread.currentThread().name}")
        Thread.sleep(5000)
        logger.info("Watermark added successfully...! ${Thread.currentThread().name}")
    }

    private fun uploadImage(imageUrl: String): Image {
        try {
            val imageSizeBytes = getImageSize(imageUrl)
            if (imageSizeBytes > 330 * 1024) {
                throw ImageSizeExceededException("Image size exceeds 330KB. Image processing aborted for $imageUrl")
            }
            val file = File(imageUrl)
            val imageBytes = Files.readAllBytes(file.toPath())
            val base64EncodedString = Base64.getEncoder().encodeToString(imageBytes)
            val imageName = file.name
            val image = firebaseStorageService.uploadImage(base64EncodedString, imageName)
            logger.info("Imaged Uploaded Successful...!")
            return image
        } catch (e: ImageSizeExceededException) {
            logger.info(e.message)
            return getDefaultImage()
        }
    }

    private fun getImageSize(imageUrl: String): Long {
        val file = File(imageUrl)
        return Files.size(file.toPath())
    }

    private fun getDefaultImage(): Image {
        return Image(url = null)
    }
}