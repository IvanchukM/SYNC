package com.example.sync.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.databinding.ActivityRegistrationBinding
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
            viewModel.register(binding.loginField, binding.passwordField)
        }

        viewModel.registrationState.observe(this, { result ->
            Toast.makeText(this, getString(result.first), Toast.LENGTH_SHORT).show()
            if (result.second) {
                onBackPressed()
            }
        })
    }
}