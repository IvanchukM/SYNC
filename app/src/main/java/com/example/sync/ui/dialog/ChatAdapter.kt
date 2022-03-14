package com.example.sync.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.R
import com.example.sync.databinding.MessageFromItemBinding
import com.example.sync.databinding.MessageToItemBinding
import com.example.sync.model.Message
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ChatAdapter(
    private val options: FirebaseRecyclerOptions<Message>,
    private val currentUser: String
) : FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CURRENT_USER) {
            val binding = MessageToItemBinding.bind(
                inflater.inflate(
                    R.layout.message_to_item,
                    parent,
                    false
                )
            )
            MessageToViewHolder(binding)
        } else {
            val binding = MessageFromItemBinding.bind(
                inflater.inflate(R.layout.message_from_item, parent, false)
            )
            MessageFromViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: Message
    ) {
        if (options.snapshots[position].messageFrom == currentUser) {
            (holder as MessageToViewHolder).bind(model)
        } else {
            (holder as MessageFromViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (options.snapshots[position].messageFrom == currentUser) VIEW_TYPE_CURRENT_USER else VIEW_TYPE_FOREIGN_USER


    companion object {
        const val VIEW_TYPE_CURRENT_USER = 1
        const val VIEW_TYPE_FOREIGN_USER = 2
    }
}

class MessageFromViewHolder(private val binding: MessageFromItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Message) {
        binding.messengerTextView.text = item.messageFrom
        binding.messageTextView.text = item.messageText
    }
}

class MessageToViewHolder(private val binding: MessageToItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Message) {
        binding.messengerTextView.text = item.messageFrom
        binding.messageTextView.text = item.messageText
    }
}