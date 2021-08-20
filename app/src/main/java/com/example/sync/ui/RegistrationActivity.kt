package com.example.sync.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sync.databinding.ActivityRegistrationBinding
import com.example.sync.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity :
    AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: ValidationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.loginField.text.toString()
            val password = binding.passwordField.text.toString()
            if (viewModel.validateEmail(binding.loginField) and viewModel.validatePassword(
                    binding.passwordField,
                    Constants.PASSWORD_REGISTRATION_PATTERN
                )
            ) {
                viewModel.register(email, password)
            }
        }

        viewModel.registrationState.observe(this, { result ->
            Toast.makeText(this, result.first, Toast.LENGTH_SHORT).show()
            if (result.second) {
                onBackPressed()
            }
        })
    }
}