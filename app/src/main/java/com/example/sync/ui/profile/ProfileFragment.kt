package com.example.sync.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.sync.databinding.FragmentProfileBinding
import com.example.sync.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    //        fab.setOnClickListener {
//            val input = findViewById<View>(R.id.input) as EditText
//
//            // Read the input field and push a new instance
//            // of ChatMessage to the Firebase database
//            FirebaseDatabase.getInstance()
//                .reference
//                .push()
//                .setValue(
//                    ChatMessage(
//                        input.text.toString(),
//                        FirebaseAuth.getInstance()
//                            .currentUser
//                            .getDisplayName()
//                    )
//                )
//
//            // Clear the input
//            input.setText("")
//        }
    companion object {
        fun newInstance() = ProfileFragment()
    }
}
