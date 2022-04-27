package com.example.sync.model

class Message {
    var messageText: String? = null
    var receiverId: String? = null
    var senderId: String? = null
    var messageSendTime: String? = null
    var roomId: String? = null

    constructor()

    constructor(
        messageText: String,
        receiverId: String,
        senderId: String,
        messageSendTime: String,
        roomId: String
    ) {
        this.messageText = messageText
        this.receiverId = receiverId
        this.senderId = senderId
        this.messageSendTime = messageSendTime
        this.roomId = roomId
    }
}
