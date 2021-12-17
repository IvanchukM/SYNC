package com.example.sync.model

class Message {
    var messageText: String? = null
    var messageTo: String? = null
    var messageFrom: String? = null
//    val messageTime: Long = Date().time

    constructor()

    constructor(messageText: String, messageTo: String, messageFrom: String) {
        this.messageText = messageText
        this.messageTo = messageTo
        this.messageFrom = messageFrom
    }
}
