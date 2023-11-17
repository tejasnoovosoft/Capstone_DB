package com.example.capstone_db.queue

import com.example.capstone_db.repository.ImageTaskRepository
import com.example.capstone_db.service.CustomException
import com.example.capstone_db.service.ImageService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Service
class ImageTaskService(
    private val imageService: ImageService,
    private val imageTaskRepository: ImageTaskRepository,
    @Qualifier("threadPoolTaskExecutor") val taskExecutor: ThreadPoolTaskExecutor
) : TaskProcessing {
    @PostConstruct
    fun init() {
        startProcessing()
    }

    fun startProcessing() {
        try {
            taskExecutor.execute {
                while (true) {
                    val imageTasks = imageTaskRepository.findByNextAttemptTimeLessThanAndStatusOrderByNextAttemptTime(
                        LocalDateTime.now(), ImageTaskStatus.PENDING, PageRequest.of(0, 10)
                    )
                    imageTasks.forEach {
                        taskExecutor.execute { processTask(it) }
                    }
                    println("Thread name: ${Thread.currentThread().name}")
                    Thread.sleep(10000)
                }
            }
        } catch (e: Exception) {
            throw CustomException(e.message)
        }
    }

    fun processTask(task: ImageTask) {
        task.lastAttemptTime = LocalDateTime.now()
        try {
            val image = task.image
            val imageData: ByteArray = Files.readAllBytes(File(image.url).toPath())
            val updatedImageData = when (task.type) {
                ImageTaskType.DOWNSCALE -> {
                    val downScaleImage = downScaleImage(imageData)
                    addWaterMark(downScaleImage)
                }

                ImageTaskType.UPSCALE -> {
                    val upScaleImage = upscaleImage(imageData)
                    addWaterMark(upScaleImage)
                }

                else -> addWaterMark(imageData)
            }
            task.status = ImageTaskStatus.SUCCESS
            val filename = image.url.substringAfterLast("/").substringBeforeLast(".")
            imageService.uploadImageToFileSystem(updatedImageData, "${filename}_${task.type}.png", filename)
        } catch (e: Exception) {
            task.status = ImageTaskStatus.ERROR
            task.lastAttemptErrorMessage = e.message
            e.printStackTrace()
        }
        task.nextAttemptTime = null
        imageTaskRepository.save(task)
    }
}