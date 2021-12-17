package com.example.sync.ui.dialoglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync.model.ChatMembers
import com.example.sync.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val dialogMembers = MutableLiveData<List<ChatMembers>>()

    init{
        getUsers()
    }

    fun getUsers() {
        repository.getUsers()
            .addOnSuccessListener { users ->
                val dialogMembers = mutableListOf<ChatMembers>()
                for (user in users) {
                    dialogMembers.add(
                        ChatMembers(
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