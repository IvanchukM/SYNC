package com.example.sync.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getCurrentTime(): String =
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
            Date()
        )

    fun ContentResolver.getName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.use { nameCursor ->
            nameCursor.moveToFirst()
            name = nameCursor.getString(nameCursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }

        return name
    }
}