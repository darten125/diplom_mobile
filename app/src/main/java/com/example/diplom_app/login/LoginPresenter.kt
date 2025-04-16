package com.example.diplom_app.login

import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.login.LoginRequest
import com.example.diplom_app.model.login.LoginResponse
import com.example.diplom_app.model.token.TokenValidationRequest
import com.example.diplom_app.model.token.TokenValidationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPresenter(private var view: LoginView?) {

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            view?.showError("Все поля должны быть заполнены!")
            return
        }
        val request = LoginRequest(
            email = email,
            password = password
        )

        RetrofitClient.instance.loginUser(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()!!.token
                        getUserDataWithToken(token)
                    } else {
                        view?.showError("Ошибка авторизации. Попробуйте ещё раз.")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
                    view?.onLoginSuccess(token, response.body()!!)
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