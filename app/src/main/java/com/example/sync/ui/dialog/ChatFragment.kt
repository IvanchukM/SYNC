package com.example.sync.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sync.databinding.FragmentDialogBinding
import com.example.sync.model.BuyOrSell
import com.example.sync.model.ChatRoomMembers
import com.example.sync.model.Message
import com.example.sync.ui.BaseFragment
import com.example.sync.utils.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val ARG_CHAT_ROOM_MEMBERS = "chatRoomMembers"

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentDialogBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter

    override fun getViewBinding() = FragmentDialogBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatRoom = arguments?.getParcelable<ChatRoomMembers>(ARG_CHAT_ROOM_MEMBERS)
        if (savedInstanceState == null) {
            chatRoom.let {
                if (it?.currentUser?.buyOrSell == BuyOrSell.SELL) {
                    viewModel.getRoomId(
                        ownerId = it.currentUser.userId,
                        userId = it.secondUser.userId
                    )
                } else {
                    viewModel.getRoomId(
                        ownerId = it?.secondUser?.userId ?: "",
                        it?.currentUser?.userId ?: ""
                    )
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatRoom = arguments?.getParcelable<ChatRoomMembers>(ARG_CHAT_ROOM_MEMBERS)

        binding.sendButton.setOnClickListener {
            if (chatRoom != null) {
                viewModel.sendMessage(
                    message = binding.messageEditText.text.toString(),
                    chatRoom
                )
            }
            binding.messageEditText.text = null
        }
        viewModel.chatRoomId.observe(viewLifecycleOwner) {
            loadMessages(it)
        }
    }

    private fun loadMessages(roomId: String) {
        lifecycleScope.launch {
            viewModel.getMessages(roomId).collect { loadingState ->
                when (loadingState) {
                    is LoadingState.Loading -> {
                    }
                    is LoadingState.Success -> {
                        initAdapter(loadingState.data)
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

    private fun initAdapter(messages: List<Message>) {
        val chatRoom = arguments?.getParcelable<ChatRoomMembers>(ARG_CHAT_ROOM_MEMBERS)

        if (chatRoom != null) {
            adapter = ChatAdapter(messages.sortedWith(compareBy {
                it.messageTime
            }), chatRoom, viewModel.uid ?: "")
            binding.messageRecyclerView.adapter = adapter
        }
    }

    companion object {
        fun newInstance(chatRoomMembers: ChatRoomMembers) = ChatFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CHAT_ROOM_MEMBERS, chatRoomMembers)
            }
        }
    }
}
