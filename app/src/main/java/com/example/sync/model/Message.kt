package com.example.sync.model

import com.example.sync.utils.Utils

data class Message(
    val messageText: String,
    val receiverId: String,
    val senderId: String,
    val messageTime: String = Utils.getCurrentTime(),
    val roomId: String
)

