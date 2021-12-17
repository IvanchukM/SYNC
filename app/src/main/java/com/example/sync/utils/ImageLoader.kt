package com.example.sync.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sync.R

fun ImageView.loadImage(imageUrl: String?){
    Glide.with(this)
        .load(imageUrl)
        .placeholder(R.drawable.ic_account_circle_black_36dp)
        .into(this)
}