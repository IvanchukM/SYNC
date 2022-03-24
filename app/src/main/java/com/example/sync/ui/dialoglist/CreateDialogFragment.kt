package com.example.sync.ui.dialoglist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sync.databinding.FragmentCreateDialogBinding
import com.example.sync.ui.BaseFragment
import com.example.sync.utils.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateDialogFragment : BaseFragment<FragmentCreateDialogBinding>(),
    CreateChatClickListener {

    private val viewModel: CreateDialogViewModel by viewModels()

    override fun getViewBinding() = FragmentCreateDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DialogMembersAdapter(this)
        binding.dialogMembers.adapter = adapter

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

    override fun createChat(userId: String) {
        viewModel.createChatRoom(userId = userId)
    }

    companion object {
        fun newInstance() = CreateDialogFragment()
    }

}