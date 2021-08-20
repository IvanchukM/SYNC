package com.example.sync.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.databinding.ActivityLoginBinding
import com.example.sync.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity :
    AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: ValidationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loginButton.setOnClickListener {
            val email = binding.loginField.text.toString()
            val password = binding.passwordField.text.toString()
            if (viewModel.validateEmail(binding.loginField) and viewModel.validatePassword(
                    binding.passwordField, Constants.PASSWORD_LOGIN_PATTERN
                )
            ) {
                viewModel.login(email, password)
            }
        }
        viewModel.authState.observe(this, { result ->
            Toast.makeText(this, result.first, Toast.LENGTH_SHORT).show()
            if (result.second) {
                onBackPressed()
            }
        })

        binding.registrationText.setOnClickListener {
            openRegistrationActivity()
        }
    }

    private fun openRegistrationActivity() {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }
}