package com.example.sync.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sync.databinding.FragmentDialogBinding
import com.example.sync.model.Message
import com.example.sync.ui.BaseFragment
import com.example.sync.utils.Constants
import com.example.sync.utils.LoadingState
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentDialogBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    private val options = FirebaseRecyclerOptions.Builder<Message>()
        .setQuery(Firebase.database.reference.child(Constants.MESSAGES), Message::class.java)
        .build()
    private lateinit var adapter: ChatAdapter
    private lateinit var username: String

    override fun getViewBinding() = FragmentDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMessages()

        binding.sendButton.setOnClickListener {
            viewModel.sendMessage(binding.messageEditText.text.toString(), username)
            binding.messageEditText.text = null
        }
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            viewModel.getUserName().collect { loadingState ->
                when (loadingState) {
                    is LoadingState.Loading -> {
                    }
                    is LoadingState.Success -> {
                        username = loadingState.data
                        initAdapter()
                        binding.messageRecyclerView.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.sendMsgLayout.isVisible = true
                    }
                    is LoadingState.Failed -> {
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = ChatAdapter(options, username)
        binding.messageRecyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
    }

    companion object {
        fun newInstance() = ChatFragment()
    }
}
