package com.example.sync.ui.dialog

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.sync.model.Message
import com.example.sync.repository.Repository
import com.example.sync.utils.Constants.Companion.MESSAGES
import com.example.sync.utils.LoadingState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.storage.ktx.storageMetadata

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val repository: Repository
) : ViewModel() {

    private var uid: String? = null
    val upload = MutableLiveData<Boolean>()

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

    fun uploadImage(uri: Uri, fileName: String) {
        val fileRef = firebaseStorage.reference.child("avatars/$fileName")

        val storageType = storageMetadata {
            contentType = "image/*"
        }
        upload.postValue(true)
        fileRef.putFile(uri, storageType)
            .addOnSuccessListener {
            upload.postValue(false)
        }
    }
}
