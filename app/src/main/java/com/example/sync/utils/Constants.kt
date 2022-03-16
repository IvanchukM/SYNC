package com.example.sync.utils

import java.util.regex.Pattern

class Constants {
    companion object {
        val PASSWORD_REGISTRATION_PATTERN: Pattern = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +  //any letter
//                "(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +  //no white spaces
                    ".{6,}" +  //at least 6 characters
                    "$"
        )
        const val UID: String = "uid"
        const val USERS: String = "users"
        const val USERNAME: String = "username"
        const val CHAT_USER_ID: String = "userId"
        const val CHAT_OWNER_ID: String = "ownerId"
        const val CHATROOM: String = "chatRoom"
        const val MESSAGES: String = "messages"
    }
}
