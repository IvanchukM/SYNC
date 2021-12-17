package com.example.sync.utils

sealed class LoadingState<T> {
    class Loading<T> : LoadingState<T>()
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Failed<T>(val message: String) : LoadingState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}