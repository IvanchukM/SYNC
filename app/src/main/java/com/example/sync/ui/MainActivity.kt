package com.example.sync.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.databinding.ActivityMainBinding
import com.example.sync.ui.dialog.ChatActivity
import com.example.sync.ui.dialoglist.ChatListActivity
import com.example.sync.ui.validation.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openLoginBtn.setOnClickListener {
            openActivity(LoginActivity::class.java)
        }
        binding.openDialogButton.setOnClickListener {
            openActivity(ChatActivity::class.java)
        }
        binding.openDialogsButton.setOnClickListener {
            openActivity(ChatListActivity::class.java)
        }
        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
        }
    }

    private fun <T> openActivity(activity: Class<T>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }


}
