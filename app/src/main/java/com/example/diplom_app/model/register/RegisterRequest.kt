package com.example.diplom_app.model.register

data class RegisterRequest(
    val name: String,
    val role: String,
    val email: String,
    val password: String,
    val userGroup: String
)
