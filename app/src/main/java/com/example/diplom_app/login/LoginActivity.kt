package com.example.diplom_app.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diplom_app.databinding.ActivityLoginBinding
import com.example.diplom_app.model.token.TokenValidationResponse
import com.example.diplom_app.registration.RegistrationActivity
import com.example.diplom_app.user.UserActivity


class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            presenter.loginUser(email, password)
        }

        binding.loginPromptTextView.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun onLoginSuccess(token: String, userData: TokenValidationResponse) {
        val prefs = getSharedPreferences("diplom_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("login_token", token).apply()

        val intent = Intent(this, UserActivity::class.java).apply {
            putExtra("id", userData.id)
            putExtra("name", userData.name)
            putExtra("email", userData.email)
            putExtra("role", userData.role)
            putExtra("currentThesisId", userData.currentThesisId)
            putExtra("userGroup", userData.userGroup)
        }
        startActivity(intent)
        finish()
    }
}