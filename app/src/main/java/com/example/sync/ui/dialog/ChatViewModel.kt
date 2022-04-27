package com.example.sync.ui.dialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync.model.ChatRoomMembers
import com.example.sync.model.Message
import com.example.sync.repository.Repository
import com.example.sync.utils.Constants.Companion.CHAT_ROOM_ID
import com.example.sync.utils.Constants.Companion.MESSAGES
import com.example.sync.utils.LoadingState
import com.example.sync.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Query
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {

    var uid: String? = null

    val chatRoomId = MutableLiveData<String>()


    val chatQuery = MutableLiveData<FirestoreRecyclerOptions<Message>>()

    private val messageList = mutableListOf<Message>()

    init {
        getUid()
    }

    fun getRoomId(ownerId: String, userId: String) {
        repository.getChatRoom(ownerId, userId)
            .addOnSuccessListener { roomData ->
                Log.d("TAG", "getRoomId: ")
                chatRoomId.postValue(roomData.documents.first().data?.get(CHAT_ROOM_ID).toString())
            }
    }

    fun sendMessage(message: String, chatRoomMembers: ChatRoomMembers) {
        val messageData = hashMapOf<String, Any?>().also {
            it["messageText"] = message
            it["receiverId"] = chatRoomMembers.secondUser.userId
            it["senderId"] = chatRoomMembers.currentUser.userId
            it["roomId"] = chatRoomId.value.toString()
            it["messageSendTime"] = Utils.getCurrentTime()
        }
        repository.sendMessage(messageData)
    }

    private fun getUid() =
        viewModelScope.launch(IO) { repository.getUid().let { uid = it } }


    fun getChatQuery(chatRoomId: String) {
        chatQuery.postValue(repository.getChatQuery(chatRoomId))
    }

    /*
     fun getMessages(roomId: String): Flow<LoadingState<List<Message>>> = channelFlow {
         send(LoadingState.loading())
         repository.getMessages(roomId)
             .addOnSuccessListener { messages ->
                 for (message in messages) {
                     Log.d("TAG", "getMessages: $message")
                     messageList.add(
                         Message(
                             messageText = message.data["messageText"].toString(),
                             receiverId = message.data["receiverId"].toString(),
                             senderId = message.data["senderId"].toString(),
                             messageSendTime = Utils.getCurrentTime(),
                             roomId = chatRoomId.value.toString()
                         )
                     )
                 }
             }.await()

         send(LoadingState.success(messageList.toList()))
     }

     fun getUserName() = flow<LoadingState<String>> {
         emit(LoadingState.loading())
         val data = repository.getUsername(uid.toString())
         emit(
             LoadingState.success(data.toString())
         )
     }.catch {
         emit(LoadingState.failed(it.message.toString()))
     }.flowOn(IO)
 */
}
