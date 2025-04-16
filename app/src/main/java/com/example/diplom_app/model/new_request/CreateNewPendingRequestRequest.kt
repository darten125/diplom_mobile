package com.example.diplom_app.model.new_request

data class CreateNewPendingRequestRequest(
    val studentId: String,
    val professorId: String,
    val thesisTitle: String,
    val description: String
)
