package com.example.diplom_app.login

import com.example.diplom_app.model.token.TokenValidationResponse

interface LoginView {
    fun showError(message: String)
    fun onLoginSuccess(token: String, userData: TokenValidationResponse)
}