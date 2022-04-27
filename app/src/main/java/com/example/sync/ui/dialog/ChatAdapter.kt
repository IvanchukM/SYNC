package com.example.sync.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sync.R
import com.example.sync.databinding.MessageFromItemBinding
import com.example.sync.databinding.MessageToItemBinding
import com.example.sync.model.ChatRoomMembers
import com.example.sync.model.Message
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(
    private val chatRoomMembers: ChatRoomMembers,
    private val currentUserId: String,
    private val options: FirestoreRecyclerOptions<Message>
) : FirestoreRecyclerAdapter<Message, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CURRENT_USER) {
            val binding = MessageToItemBinding.bind(
                inflater.inflate(
                    R.layout.message_to_item,
                    parent,
                    false
                )
            )
            MessageToViewHolder(binding, chatRoomMembers)
        } else {
            val binding = MessageFromItemBinding.bind(
                inflater.inflate(R.layout.message_from_item, parent, false)
            )
            MessageFromViewHolder(binding, chatRoomMembers)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        model: Message
    ) {
        if (options.snapshots[position].senderId == currentUserId) {
            (holder as MessageToViewHolder).bind(model)
        } else {
            (holder as MessageFromViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (options.snapshots[position].senderId == currentUserId) VIEW_TYPE_CURRENT_USER else VIEW_TYPE_FOREIGN_USER


    companion object {
        const val VIEW_TYPE_CURRENT_USER = 1
        const val VIEW_TYPE_FOREIGN_USER = 2
    }
}

class MessageFromViewHolder(
    private val binding: MessageFromItemBinding,
    private val chatRoomMembers: ChatRoomMembers
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Message) {
        binding.messengerTextView.text = chatRoomMembers.secondUser.username
        binding.messageTextView.text = item.messageText
    }
}

class MessageToViewHolder(
    private val binding: MessageToItemBinding,
    private val chatRoomMembers: ChatRoomMembers
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Message) {
        binding.messengerTextView.text = chatRoomMembers.currentUser.username
        binding.messageTextView.text = item.messageText
    }
}