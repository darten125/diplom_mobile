package com.example.diplom_app.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diplom_app.R
import com.example.diplom_app.databinding.ActivityRegistrationBinding
import com.example.diplom_app.login.LoginActivity
import com.example.diplom_app.model.token.TokenValidationResponse
import com.example.diplom_app.user.UserActivity

class RegistrationActivity : AppCompatActivity(), RegistrationView {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var presenter: RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegistrationPresenter(this)

        val educationLevels = listOf("Бакалавриат", "Магистратура")
        val adapter = ArrayAdapter(this, R.layout.education_level_list_item, educationLevels)
        binding.educationSpinner.setAdapter(adapter)

        binding.registerButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            var education = binding.educationSpinner.text.toString().trim()
            education = when (education) {
                "Бакалавриат" -> "bac"
                "Магистратура" -> "mag"
                else -> education
            }
            val group = binding.groupEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            presenter.registerUser(fullName, education, group, email, password)
        }

        binding.loginPromptTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun onRegistrationSuccess(token: String, userData: TokenValidationResponse) {
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