package com.example.sync.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoomMembers(
    val currentUser: UserModel,
    val secondUser: UserModel
) : Parcelable