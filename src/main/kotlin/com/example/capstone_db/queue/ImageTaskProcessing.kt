package com.example.capstone_db.queue

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.imgscalr.Scalr
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


interface ImageTaskProcessing {
    fun addWaterMark(inputImageBytes: ByteArray, watermarkImageBytes: ByteArray): ByteArray {
        println("WaterMarking image in Thread: ${Thread.currentThread().name}")

        val inputImageStream = ByteArrayInputStream(inputImageBytes)
        val watermarkImageStream = ByteArrayInputStream(watermarkImageBytes)

        val inputImage: BufferedImage = ImageIO.read(inputImageStream)
        var watermarkImage: BufferedImage = ImageIO.read(watermarkImageStream)

        watermarkImage = Thumbnails.of(watermarkImage)
            .size(100, 100)
            .asBufferedImage()

        val outputStream = ByteArrayOutputStream()
        Thumbnails.of(inputImage)
            .size(inputImage.width, inputImage.height)
            .watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f)
            .outputQuality(1.0)
            .outputFormat("png")
            .toOutputStream(outputStream)
        return outputStream.toByteArray()
    }

    fun upscaleImage(inputImageBytes: ByteArray, targetWidth: Int, targetHeight: Int): ByteArray {
        println("UpScaling image in Thread: ${Thread.currentThread().name}")
        val inputImageStream = ByteArrayInputStream(inputImageBytes)
        val inputImage: BufferedImage = ImageIO.read(inputImageStream)
        val scaledImage: BufferedImage =
            Scalr.resize(inputImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight)

        val outputImageStream = ByteArrayOutputStream()
        ImageIO.write(scaledImage, "png", outputImageStream)

        return outputImageStream.toByteArray()
    }

    fun downScaleImage(inputImageBytes: ByteArray, targetWidth: Int, targetHeight: Int): ByteArray {
        println("DownScaling image in Thread: ${Thread.currentThread().name}")
        val inputImageStream = ByteArrayInputStream(inputImageBytes)
        val inputImage: BufferedImage = ImageIO.read(inputImageStream)
        val scaledImage: BufferedImage =
            Scalr.resize(inputImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight)

        val outputImageStream = ByteArrayOutputStream()
        ImageIO.write(scaledImage, "png", outputImageStream)
        return outputImageStream.toByteArray()
    }
}