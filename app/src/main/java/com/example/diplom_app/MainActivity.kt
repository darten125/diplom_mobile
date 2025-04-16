package com.example.diplom_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.databinding.ActivityMainBinding
import com.example.diplom_app.model.token.TokenValidationRequest
import com.example.diplom_app.model.token.TokenValidationResponse
import com.example.diplom_app.registration.RegistrationActivity
import com.example.diplom_app.user.UserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkTokenValidity()
    }

    private fun checkTokenValidity() {
        val prefs = getSharedPreferences("diplom_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("login_token", null)

        if (token.isNullOrEmpty()) {
            navigateToRegistration()
        } else {
            val request = TokenValidationRequest(token)
            RetrofitClient.instance.validateToken(request).enqueue(object : Callback<TokenValidationResponse> {
                override fun onResponse(
                    call: Call<TokenValidationResponse>,
                    response: Response<TokenValidationResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        navigateToUserInfo(response.body()!!)
                    } else {
                        navigateToRegistration()
                    }
                }

                override fun onFailure(call: Call<TokenValidationResponse>, t: Throwable) {
                    navigateToRegistration()
                }
            })
        }
    }

    private fun navigateToRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }

    private fun navigateToUserInfo(tokenResponse: TokenValidationResponse) {
        val intent = Intent(this, UserActivity::class.java).apply {
            putExtra("id", tokenResponse.id)
            putExtra("name", tokenResponse.name)
            putExtra("email", tokenResponse.email)
            putExtra("password", tokenResponse.password)
            putExtra("role", tokenResponse.role)
            putExtra("currentThesisId", tokenResponse.currentThesisId)
            putExtra("userGroup", tokenResponse.userGroup)
        }
        startActivity(intent)
        finish()
    }
}