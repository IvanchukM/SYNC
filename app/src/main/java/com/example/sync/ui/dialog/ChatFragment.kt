package com.example.sync.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.sync.databinding.FragmentDialogBinding
import com.example.sync.model.BuyOrSell
import com.example.sync.model.ChatRoomMembers
import com.example.sync.model.Message
import com.example.sync.ui.BaseFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint

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

        showProgressBar()

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
            viewModel.getChatQuery(it)
            hideProgressBar()
        }
        viewModel.chatQuery.observe(viewLifecycleOwner) { query ->
            initMessageLoading(query)
        }
    }

    private fun initMessageLoading(query: FirestoreRecyclerOptions<Message>) {
        initAdapter(query)
    }

//    private fun loadMessages(roomId: String, query: Query) {
//        lifecycleScope.launch {
//            viewModel.getMessages(roomId).collect { loadingState ->
//                when (loadingState) {
//                    is LoadingState.Loading -> {
//                    }
//                    is LoadingState.Success -> {
//                        initAdapter(loadingState.data)
//                        binding.messageRecyclerView.isVisible = true
//                        binding.progressBar.isVisible = false
//                        binding.sendMsgLayout.isVisible = true
//                    }
//                    is LoadingState.Failed -> {
//                    }
//                }
//            }
//        }
//    }

    private fun showProgressBar() {
        binding.messageRecyclerView.isVisible = false
        binding.progressBar.isVisible = true
        binding.sendMsgLayout.isVisible = false
    }

    private fun hideProgressBar() {
        binding.messageRecyclerView.isVisible = true
        binding.progressBar.isVisible = false
        binding.sendMsgLayout.isVisible = true
    }

    private fun initAdapter(options: FirestoreRecyclerOptions<Message>) {
        val chatRoom = arguments?.getParcelable<ChatRoomMembers>(ARG_CHAT_ROOM_MEMBERS)

        if (chatRoom != null) {
            adapter = ChatAdapter(chatRoom, viewModel.uid ?: "", options)
            binding.messageRecyclerView.adapter = adapter
        }
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        fun newInstance(chatRoomMembers: ChatRoomMembers) = ChatFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CHAT_ROOM_MEMBERS, chatRoomMembers)
            }
        }
    }
}
