package com.example.sync.ui.dialoglist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sync.databinding.ActivityDialogListBinding
import com.example.sync.model.ChatMembers
import com.example.sync.ui.BaseViewModelActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateDialogActivity : BaseViewModelActivity<ActivityDialogListBinding>(),
    CreateChatClickListener {

    private val viewModel: CreateDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DialogMembersAdapter(this)
        binding.dialogMembers.adapter = adapter

        viewModel.dialogMembers.observe(this, {
            adapter.setDialogMembers(it)
        })

    }

    override fun getViewBinding(): ActivityDialogListBinding =
        ActivityDialogListBinding.inflate(layoutInflater)

    override fun createChat(userId: String) {
        // pass ownerId via intent
        // now uid = Nick uid
        viewModel.createChatRoom(userId = userId, ownerId = "pMY7VMwHPTgHX02u5GmWkEuQ74M2")
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
    }

}