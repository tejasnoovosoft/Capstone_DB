package com.example.capstone_db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class CapstoneDbApplication

fun main(args: Array<String>) {
    runApplication<CapstoneDbApplication>(*args)
}
