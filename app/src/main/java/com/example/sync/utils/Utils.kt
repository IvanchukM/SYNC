package com.example.sync.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getCurrentTime(): String =
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
            Date()
        )

}