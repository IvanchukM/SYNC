package com.example.sync.ui.dialoglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.databinding.DialogListItemBinding
import com.example.sync.model.UserModel
import com.example.sync.utils.loadImage

class DialogMembersAdapter(private val onUserClickListener: OnUserClickListener) :
    RecyclerView.Adapter<DialogListVIewHolder>() {

    private var chatMembers: List<UserModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListVIewHolder =
        DialogListVIewHolder(
            DialogListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            onUserClickListener
        )

    override fun onBindViewHolder(holder: DialogListVIewHolder, position: Int) {
        holder.bind(chatMembers[position])
    }

    override fun getItemCount(): Int = chatMembers.size

    fun setDialogMembers(chatMembers: List<UserModel>) {
        this.chatMembers = chatMembers
        notifyDataSetChanged()
    }

}

class DialogListVIewHolder(
    private val binding: DialogListItemBinding,
    private val onUserClickListener: OnUserClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        userModel: UserModel
    ) {
        binding.userProfileImage.loadImage(userModel.userProfileImage)
        binding.username.text = userModel.username

        binding.user.setOnClickListener {
            onUserClickListener.openChat(
                userModel = userModel
            )
        }
    }
}

interface OnUserClickListener {
    fun openChat(
        userModel: UserModel
    )
}