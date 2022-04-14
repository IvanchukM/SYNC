package com.example.sync.ui.dialoglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync.model.BuyOrSell
import com.example.sync.model.UserModel
import com.example.sync.repository.Repository
import com.example.sync.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CreateDialogViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val currentUserId = MutableLiveData<String>()

    val currentUserInfo = MutableLiveData<UserModel>()

    init {
        currentUserId.value = repository.getUid()
        viewModelScope.launch {
            getCurrentUserInfo(repository.getUid())
        }
    }

    fun openChatRoom(secondUserId: String) {
        val currentUser: String = this.currentUserId.value.toString()
        val ownerId: String
        val userId: String

        // check if current user is buyer or seller
        if (currentUserInfo.value?.buyOrSell == BuyOrSell.SELL) {
            ownerId = currentUser
            userId = secondUserId
        } else {
            ownerId = secondUserId
            userId = currentUser
        }

        val chatRoom = hashMapOf<String, Any?>().also {
            it["ownerId"] = ownerId
            it["userId"] = userId
        }
        repository.getChatRoom(ownerId, userId)
            .addOnSuccessListener { chatRooms ->
                if (chatRooms.documents.isEmpty()) {
                    repository.createChatRoom(chatRoom)
                } else {
                    Log.d("TAG", "Chat is already exist")
                }
            }
    }

    private suspend fun getCurrentUserInfo(currentUser: String) {
        val currentUserName = getUserName(currentUser)
        // temp for test Nick only
        if (currentUserName.trim() == "Nick") {
            currentUserInfo.postValue(
                UserModel(
                    userId = currentUser,
                    username = currentUserName,
                    buyOrSell = BuyOrSell.SELL
                )
            )
        } else {
            currentUserInfo.postValue(
                UserModel(
                    userId = currentUser,
                    username = currentUserName,
                    buyOrSell = BuyOrSell.BUY
                )
            )
        }
    }

    private suspend fun getUserName(userId: String): String =
        repository.getUsername(userId).toString()

    fun getUsers(): Flow<LoadingState<MutableList<UserModel>>> = channelFlow {
        val currentUser = currentUserId.value.toString()
        send(LoadingState.loading())
        val dialogMembers = mutableListOf<UserModel>()
        repository.getUsers(currentUser)
            .addOnSuccessListener { users ->
                for (user in users) {
                    dialogMembers.add(
                        UserModel(
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
