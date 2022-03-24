package com.example.sync.ui.dialoglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync.model.ChatMembers
import com.example.sync.repository.Repository
import com.example.sync.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CreateDialogViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val ownerId = MutableLiveData<String>()

    init {
        ownerId.value = repository.getUid()
    }

    fun createChatRoom(userId: String) {
        val ownerId: String = this.ownerId.value.toString()
        val chatRoom = hashMapOf<String, Any?>().also {
            it["ownerId"] = ownerId
            it["userId"] = userId
        }

        repository.checkIfChatRoomAlreadyExist(ownerId, userId)
            .addOnSuccessListener { chatRooms ->
                if (chatRooms.documents.isEmpty()) {
                    repository.createChatRoom(chatRoom)
                } else {
                    Log.d("TAG", "Chat is already exist")
                }
            }
    }

    fun getUsers(): Flow<LoadingState<MutableList<ChatMembers>>> = channelFlow {
        val currentUser = ownerId.value.toString()
        send(LoadingState.loading())
        val dialogMembers = mutableListOf<ChatMembers>()
        repository.getUsers(currentUser)
            .addOnSuccessListener { users ->
                for (user in users) {
                    dialogMembers.add(
                        ChatMembers(
                            userId = user.data["uid"].toString(),
                            userProfileImage = user.data["profilePicture"].toString(),
                            username = user.data["username"].toString()
                        )
                    )
                }
            }.await()
        send(LoadingState.success(dialogMembers))
    }
}
