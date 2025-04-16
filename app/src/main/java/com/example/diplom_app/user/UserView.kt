package com.example.diplom_app.user

import com.example.diplom_app.model.user.GetCurrentThesisResponse

interface UserView {
    fun displayCurrentThesis(request: GetCurrentThesisResponse)
    fun displayRequests(requests: List<ThesisRequestModel>)
    fun displayRequestsAndFinalThesisButton(requests: List<ThesisRequestModel>)
    fun displayEmptyRequests()
    fun showError(message: String)
    fun showLoading()
    fun hideLoading()
}