package com.example.sync.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    val userId: String,
    val username: String,
    val userProfileImage: String? = null,
    val buyOrSell: BuyOrSell? = null
) : Parcelable

enum class BuyOrSell {
    BUY,
    SELL
}