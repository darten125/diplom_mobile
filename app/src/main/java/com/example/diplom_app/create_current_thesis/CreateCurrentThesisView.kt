package com.example.diplom_app.create_current_thesis

import com.example.diplom_app.model.user.ProcessedRequestItem

interface CreateCurrentThesisView {
    fun showApprovedRequests(requests: List<ProcessedRequestItem>)
    fun showError(message: String)
    fun onThesisCreated(message: String)
}