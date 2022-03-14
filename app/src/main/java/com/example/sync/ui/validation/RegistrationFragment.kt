package com.example.sync.ui.validation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.sync.databinding.FragmentRegistrationBinding
import com.example.sync.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val viewModel: ValidationViewModel by viewModels()

    override fun getViewBinding() = FragmentRegistrationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            viewModel.register(binding.loginField, binding.passwordField, binding.usernameField)
        }

        viewModel.registrationState.observe(viewLifecycleOwner) { result ->
            if (result.second) {
                parentFragmentManager.popBackStack()
            }
        }
    }
    companion object{
        fun newInstance() = RegistrationFragment()
    }
}
