package com.example.sync.ui.dialoglist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sync.R
import com.example.sync.databinding.FragmentCreateDialogBinding
import com.example.sync.model.ChatRoomMembers
import com.example.sync.model.UserModel
import com.example.sync.ui.BaseFragment
import com.example.sync.ui.dialog.ChatFragment
import com.example.sync.utils.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateDialogFragment : BaseFragment<FragmentCreateDialogBinding>(),
    OnUserClickListener {

    private val viewModel: CreateDialogViewModel by viewModels()
    private lateinit var chatRoomMembers: ChatRoomMembers

    override fun getViewBinding() = FragmentCreateDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DialogMembersAdapter(this)
        binding.dialogMembers.adapter = adapter

//        viewModel.chatRoom.observe(viewLifecycleOwner) {
//            chatRoomMembers = it
//        }


        lifecycleScope.launch {
            viewModel.getUsers().collect { loadingState ->
                when (loadingState) {
                    is LoadingState.Loading -> {
                        showLoading()
                    }
                    is LoadingState.Success -> {
                        adapter.setDialogMembers(loadingState.data)
                        hideLoading()
                    }
                    is LoadingState.Failed -> {
                        showLoading()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.dialogMembers.isVisible = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.dialogMembers.visibility = View.VISIBLE
    }

    private fun redirectToChatScreen() {
        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                ChatFragment.newInstance(chatRoomMembers)
            )
            .commit()
    }

    override fun openChat(userModel: UserModel) {
        val currentUserData: UserModel = viewModel.currentUserInfo.value as UserModel
        viewModel.openChatRoom(secondUserId = userModel.userId)
        chatRoomMembers = ChatRoomMembers(
            currentUser = currentUserData,
            secondUser = userModel
        )
        redirectToChatScreen()
    }

    companion object {
        fun newInstance() = CreateDialogFragment()
    }

}