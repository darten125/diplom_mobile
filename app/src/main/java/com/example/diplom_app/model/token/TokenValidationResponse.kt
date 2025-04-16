package com.example.diplom_app.model.token

data class TokenValidationResponse(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val currentThesisId: String?,
    val userGroup: String
)
