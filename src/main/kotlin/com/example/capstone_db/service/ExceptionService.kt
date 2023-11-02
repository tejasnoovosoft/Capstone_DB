package com.example.capstone_db.service

class UserNotFoundException(message: String) : RuntimeException(message)

class ProductNotFoundException(message: String) : RuntimeException(message)

class UserExistsException(message: String) : RuntimeException(message)

data class FileResponse(val filename: String?, val message: String?)
