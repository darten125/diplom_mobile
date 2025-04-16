package com.example.diplom_app.registration

import com.example.diplom_app.model.token.TokenValidationResponse

interface RegistrationView {
    fun showError(message: String)
    fun onRegistrationSuccess(token: String, userData: TokenValidationResponse)
}