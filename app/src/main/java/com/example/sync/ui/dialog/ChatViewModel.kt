package com.example.sync.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync.model.Message
import com.example.sync.repository.Repository
import com.example.sync.utils.Constants.Companion.MESSAGES
import com.example.sync.utils.LoadingState
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val repository: Repository
) : ViewModel() {

    private var uid: String? = null

    init {
        getUid()
    }

    fun sendMessage(message: String, username: String) {
        firebaseDatabase
            .reference
            .child(MESSAGES)
            .push()
            .setValue(
                Message(
                    messageText = message,
                    messageTo = "TestName",
                    messageFrom = username
                )
            )
    }

    private fun getUid() =
        viewModelScope.launch(IO) { repository.getUid().let { uid = it } }

    fun getUserName() = flow<LoadingState<String>> {
        emit(LoadingState.loading())
        val data = repository.getUsername(uid.toString())
        emit(
            LoadingState.success(data.toString())
        )
    }.catch {
        emit(LoadingState.failed(it.message.toString()))
    }.flowOn(IO)

}
