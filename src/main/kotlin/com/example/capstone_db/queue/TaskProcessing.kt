package com.example.capstone_db.queue

interface TaskProcessing {
    fun addWaterMark(imageData: ByteArray): ByteArray {
        println("WaterMarking image in Thread: ${Thread.currentThread().name}")
        Thread.sleep(5000)
        return imageData
    }

    fun upscaleImage(imageData: ByteArray): ByteArray {
        println("UpScaling image in Thread: ${Thread.currentThread().name}")
        Thread.sleep(5000)
        return imageData
    }

    fun downScaleImage(imageData: ByteArray): ByteArray {
        println("DownScaling image in Thread: ${Thread.currentThread().name}")
        Thread.sleep(5000)
        return imageData
    }
}