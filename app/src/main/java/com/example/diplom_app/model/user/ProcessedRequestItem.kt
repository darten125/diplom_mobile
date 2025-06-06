package com.example.diplom_app.model.user

data class ProcessedRequestItem(
    val id: String,
    val professorName: String,
    val professorPosition: String,
    val professorDepartment: String,
    val thesisTitle: String,
    val description: String,
    val accepted: Boolean
)
