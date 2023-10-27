package com.example.capstone_db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class CapstoneDbApplication

fun main(args: Array<String>) {
    runApplication<CapstoneDbApplication>(*args)
}
