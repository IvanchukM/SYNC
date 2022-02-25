package com.example.sync.ui.dialoglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync.model.ChatMembers
import com.example.sync.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateDialogViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val dialogMembers = MutableLiveData<List<ChatMembers>>()

    init {
        getUsers()
    }

    fun createChatRoom(userId: String, ownerId: String) {
        val chatRoom = hashMapOf<String, Any?>().also {
            it["ownerId"] = ownerId
            it["userId"] = userId
        }
        repository.createChatRoom(chatRoom)
    }

    private fun getUsers() {
        repository.getUsers()
            .addOnSuccessListener { users ->
                val dialogMembers = mutableListOf<ChatMembers>()
                for (user in users) {
                    dialogMembers.add(
                        ChatMembers(
                            userId = user.data["uid"].toString(),
                            userProfileImage = user.data["profilePicture"].toString(),
                            username = user.data["username"].toString()
                        )
                    )
                    this.dialogMembers.value = dialogMembers
                    Log.d("TAG", "getUsers: ${user.data}")
                }
            }
    }
}