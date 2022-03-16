package com.example.sync.ui.dialoglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.sync.databinding.FragmentCreateDialogBinding
import com.example.sync.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDialogFragment : BaseFragment<FragmentCreateDialogBinding>(),
    CreateChatClickListener {

    private val viewModel: CreateDialogViewModel by viewModels()

    override fun getViewBinding() = FragmentCreateDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DialogMembersAdapter(this)
        binding.dialogMembers.adapter = adapter

        viewModel.dialogMembers.observe(viewLifecycleOwner) {
            adapter.setDialogMembers(it)
        }
    }

    override fun createChat(userId: String) {
        // pass ownerId via intent
        // now uid = Nick uid
        viewModel.createChatRoom(userId = userId, ownerId = "V8dno26vkkXyi0hLGWCXfnNTdFC2")
    }

    companion object {
        fun newInstance() = CreateDialogFragment()
    }

}