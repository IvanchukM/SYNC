package com.example.sync.ui.mainScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.sync.R
import com.example.sync.databinding.FragmentMainScreenBinding
import com.example.sync.ui.BaseFragment
import com.example.sync.ui.dialog.ChatFragment
import com.example.sync.ui.dialoglist.CreateDialogFragment
import com.example.sync.ui.validation.LoginFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainScreenFragment : BaseFragment<FragmentMainScreenBinding>() {

    override fun getViewBinding() = FragmentMainScreenBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openLoginBtn.setOnClickListener {
            openFragment(LoginFragment.newInstance())
        }
        binding.openDialogButton.setOnClickListener {
            openFragment(ChatFragment.newInstance())
        }
        binding.createDialogButton.setOnClickListener {
            openFragment(CreateDialogFragment.newInstance())
        }
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
        }
    }

    private fun openFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                fragment
            )
            .commit()
    }

    companion object {
        fun newInstance() = MainScreenFragment()
    }
}