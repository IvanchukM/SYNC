package com.example.sync.ui.validation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.sync.R
import com.example.sync.databinding.FragmentLoginBinding
import com.example.sync.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: ValidationViewModel by viewModels()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.login(binding.loginField, binding.passwordField)
        }
        viewModel.authState.observe(
            viewLifecycleOwner
        ) { result ->
            if (result.second) {
                parentFragmentManager.popBackStack()
            }

        }
        binding.registrationText.setOnClickListener{
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    RegistrationFragment.newInstance()
                )
                .addToBackStack(null)
                .commit()
        }
    }
    companion object{
        fun newInstance() = LoginFragment()
    }
}
