package com.example.diplom_app.registration

import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.register.RegisterRequest
import com.example.diplom_app.model.register.RegisterResponse
import com.example.diplom_app.model.token.TokenValidationRequest
import com.example.diplom_app.model.token.TokenValidationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationPresenter(private var view: RegistrationView?) {

    fun registerUser(fullName: String, education: String, group: String, email: String, password: String) {

        if (fullName.isBlank() || education.isBlank() || group.isBlank() || email.isBlank() || password.isBlank()) {
            view?.showError("Все поля должны быть заполнены!")
            return
        }
        val request = RegisterRequest(
            name = fullName,
            role = education,
            email = email,
            password = password,
            userGroup = group
        )

        RetrofitClient.instance.registerUser(request)
            .enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    getUserDataWithToken(token)
                } else {
                    view?.showError("Ошибка регистрации. Попробуйте ещё раз.")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                view?.showError("Ошибка соединения: ${t.localizedMessage}")
            }
        })
    }

    fun getUserDataWithToken(token: String) {
        val request = TokenValidationRequest(token)
        RetrofitClient.instance.validateToken(request).enqueue(object : Callback<TokenValidationResponse> {
            override fun onResponse(
                call: Call<TokenValidationResponse>,
                response: Response<TokenValidationResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    view?.onRegistrationSuccess(token, response.body()!!)
                } else {
                    view?.showError("Ошибка получения данных пользователя")
                }
            }

            override fun onFailure(call: Call<TokenValidationResponse>, t: Throwable) {
                view?.showError("Ошибка соединения: ${t.localizedMessage}")
            }
        })
    }

    fun detach() {
        view = null
    }
}