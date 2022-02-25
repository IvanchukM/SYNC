package com.example.sync.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.sync.databinding.ActivityDialogBinding
import com.example.sync.model.Message
import com.example.sync.ui.BaseViewModelActivity
import com.example.sync.utils.Constants
import com.example.sync.utils.LoadingState
import com.example.sync.utils.getName
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : BaseViewModelActivity<ActivityDialogBinding>() {

    private val viewModel: ChatViewModel by viewModels()
    private val options = FirebaseRecyclerOptions.Builder<Message>()
        .setQuery(Firebase.database.reference.child(Constants.MESSAGES), Message::class.java)
        .build()

    // private lateinit var adapter: ChatAdapter
    private lateinit var username: String

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.uploadImage(uri, fileName = this.contentResolver.getName(uri))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.sendButton.setOnClickListener {
//            viewModel.sendMessage(binding.messageEditText.text.toString(), username)
//            binding.messageEditText.text = null
//        }

        binding.uploadBtnImg.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.uploadBtnMusic.setOnClickListener {
            getContent.launch("audio/*")
        }

        viewModel.upload.observe(this) {
            if (it) {
                Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show()
            }
        }
    }
/*
    private fun loadMessages() {
        lifecycleScope.launch {
            viewModel.getUserName().collect { loadingState ->
                when (loadingState) {
                    is LoadingState.Loading -> {
                        showToast("Loading")
                    }
                    is LoadingState.Success -> {
                        showToast(loadingState.data)
                        username = loadingState.data
                        initAdapter()
                        binding.messageRecyclerView.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.sendMsgLayout.isVisible = true
                    }
                    is LoadingState.Failed -> {
                        showToast("Failed ${loadingState.message}")
                    }
                }
            }
        }
    }
*/

//    private fun initAdapter() {
//        adapter = ChatAdapter(options, username)
//        binding.messageRecyclerView.adapter = adapter
//        adapter.startListening()
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        adapter.stopListening()
//    }

    override fun getViewBinding(): ActivityDialogBinding =
        ActivityDialogBinding.inflate(layoutInflater)

}

