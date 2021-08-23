package com.example.sync.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: ValidationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            viewModel.login(binding.loginField, binding.passwordField)
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