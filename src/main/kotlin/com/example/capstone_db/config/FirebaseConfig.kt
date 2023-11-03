package com.example.capstone_db.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfig {

    @Bean
    fun initializeFirebaseApp(): FirebaseApp {
        val serviceAccount = ClassPathResource("config/serviceKey.json").inputStream
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("capstone-db-6a168.appspot.com")
            .build()

        return FirebaseApp.initializeApp(options)
    }
}

