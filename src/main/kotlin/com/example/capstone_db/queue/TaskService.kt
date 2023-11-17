package com.example.capstone_db.queue

import com.example.capstone_db.model.Status
import com.example.capstone_db.repository.EmailTaskRepository
import com.example.capstone_db.repository.ImageTaskRepository
import com.example.capstone_db.repository.TaskRepository
import com.example.capstone_db.service.CustomException
import com.example.capstone_db.service.ImageService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    @Qualifier("threadPoolTaskExecutor") val taskExecutor: ThreadPoolTaskExecutor,
    private val imageService: ImageService,
    private val imageTaskRepository: ImageTaskRepository,
    private val emailTaskRepository: EmailTaskRepository
) : ImageTaskProcessing {

    @Value("\${watermark}")
    private lateinit var watermark: String

    @PostConstruct
    fun init() {
        startProcessing()
    }

    fun startProcessing() {
        try {
            taskExecutor.execute {
                while (true) {
                    val tasks = taskRepository.findByNextAttemptTimeLessThanAndTaskStatusOrderByNextAttemptTime(
                        LocalDateTime.now(), Status.PENDING, PageRequest.of(0, 10)
                    )

                    tasks.forEach {
                        processTask(it)
                    }

                    println("Thread name: ${Thread.currentThread().name}")
                    Thread.sleep(10000)
                }
            }
        } catch (e: Exception) {
            throw CustomException(e.message)
        }
    }

    fun processTask(task: Task) {
        task.lastAttemptTime = LocalDateTime.now()
        try {
            when (task) {
                is ImageTask -> imageTask(task)
                is EmailTask -> emailTask(task)
            }
            task.taskStatus = Status.SUCCESS
        } catch (e: Exception) {
            task.taskStatus = Status.ERROR
            task.lastAttemptErrorMessage = e.message
            e.printStackTrace()
        }
        task.nextAttemptTime = null
        taskRepository.save(task)
    }

    fun imageTask(task: ImageTask) {
        try {
            val image = task.image
            val imageData: ByteArray = Files.readAllBytes(File(image.url).toPath())
            val watermarkImage: ByteArray = Files.readAllBytes(File(watermark).toPath())
            val updatedImageData = when (task.type) {
                ImageTaskType.DOWNSCALE -> {
                    val downScaleImage = downScaleImage(imageData, 640, 640)
                    addWaterMark(downScaleImage, watermarkImage)
                }

                ImageTaskType.UPSCALE -> {
                    val upScaleImage = upscaleImage(imageData, 1000, 1000)
                    addWaterMark(upScaleImage, watermarkImage)
                }

                else -> addWaterMark(imageData, watermarkImage)
            }
            task.status = Status.SUCCESS
            val filename = image.url.substringAfterLast("/").substringBeforeLast(".")
            imageService.uploadImageToFileSystem(updatedImageData, "${filename}_${task.type}.png", filename)
        } catch (e: Exception) {
            task.status = Status.SUCCESS
            e.printStackTrace()
        }
        imageTaskRepository.save(task)
    }

    fun emailTask(task: EmailTask) {
        try {
            task.status = Status.SUCCESS
        } catch (e: Exception) {
            task.status = Status.ERROR
            e.printStackTrace()
        }
        emailTaskRepository.save(task)
    }
}