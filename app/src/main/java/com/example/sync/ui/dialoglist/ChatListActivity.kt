package com.example.sync.ui.dialoglist

import android.os.Bundle
import androidx.activity.viewModels
import com.example.sync.databinding.ActivityDialogListBinding
import com.example.sync.ui.BaseViewModelActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListActivity : BaseViewModelActivity<ActivityDialogListBinding>() {

    private val viewModel: ChatListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DialogMembersAdapter()
        binding.dialogMembers.adapter = adapter

        viewModel.dialogMembers.observe(this, {
            adapter.setDialogMembers(it)
        })

    }

    override fun getViewBinding(): ActivityDialogListBinding =
        ActivityDialogListBinding.inflate(layoutInflater)

}