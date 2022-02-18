package com.example.sync.ui.dialoglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.databinding.DialogListItemBinding
import com.example.sync.model.ChatMembers
import com.example.sync.model.ChatRoom
import com.example.sync.utils.loadImage

class DialogMembersAdapter(private val createChatClickListener: CreateChatClickListener) :
    RecyclerView.Adapter<DialogListVIewHolder>() {

    private var chatMembers: List<ChatMembers> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListVIewHolder =
        DialogListVIewHolder(
            DialogListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            createChatClickListener
        )

    override fun onBindViewHolder(holder: DialogListVIewHolder, position: Int) {
        holder.bind(chatMembers[position])
    }

    override fun getItemCount(): Int = chatMembers.size

    fun setDialogMembers(chatMembers: List<ChatMembers>) {
        this.chatMembers = chatMembers
        notifyDataSetChanged()
    }

}

class DialogListVIewHolder(
    private val binding: DialogListItemBinding,
    private val createChatClickListener: CreateChatClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        chatMembers: ChatMembers
    ) {
        binding.userProfileImage.loadImage(chatMembers.userProfileImage)
        binding.username.text = chatMembers.username

        binding.username.setOnClickListener {
            createChatClickListener.createChat(
                userId = chatMembers.userId
            )
        }
    }
}

interface CreateChatClickListener {
    fun createChat(
        userId: String
    )
}