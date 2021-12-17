package com.example.sync.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}