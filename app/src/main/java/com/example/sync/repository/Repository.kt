package com.example.sync.repository

import android.net.Uri
import com.example.sync.model.Message
import com.example.sync.utils.Constants
import com.example.sync.utils.Constants.Companion.CHATROOM
import com.example.sync.utils.Constants.Companion.CHAT_OWNER_ID
import com.example.sync.utils.Constants.Companion.CHAT_ROOM_ID
import com.example.sync.utils.Constants.Companion.CHAT_USER_ID
import com.example.sync.utils.Constants.Companion.MESSAGES
import com.example.sync.utils.Constants.Companion.USERNAME
import com.example.sync.utils.Constants.Companion.USERS
import com.example.sync.utils.ContentType
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.tasks.await
import retrofit2.http.Url
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

private const val URL =
    "https://console.firebase.google.com/u/2/project/sync-29d47/storage/sync-29d47.appspot.com/files/~2Fsamples"
private const val MEGABYTES: Long = 1024 * 1024 * 20

@Singleton
class Repository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {

    suspend fun authenticate(
        email: String,
        password: String
    ): AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String): AuthResult =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()

    fun addUserData(currentUser: String, userData: HashMap<String, Any?>) {
        firebaseFirestore.collection(USERS).document(currentUser)
            .set(userData)
    }

    fun getUid(): String = firebaseAuth.currentUser?.uid.toString()

    suspend fun getUsername(uid: String) =
        firebaseFirestore
            .collection(USERS)
            .document(uid)
            .get()
            .await()
            .get(USERNAME)


    fun getUsers(currentUser: String): Task<QuerySnapshot> =
        firebaseFirestore.collection(USERS)
            .whereNotEqualTo("uid", currentUser)
            .get()

    fun getChatRoom(ownerId: String, userId: String): Task<QuerySnapshot> =
        firebaseFirestore.collection(CHATROOM)
            .whereEqualTo(
                CHAT_OWNER_ID, ownerId
            )
            .whereEqualTo(CHAT_USER_ID, userId)
            .get()

    fun getChatQuery(roomId: String): FirestoreRecyclerOptions<Message> =
        FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(
                firebaseFirestore.collection(MESSAGES)
                    .whereEqualTo(CHAT_ROOM_ID, roomId)
                    .orderBy("messageSendTime", Query.Direction.DESCENDING),
                Message::class.java
            )
            .build()

    fun createChatRoom(chatRoom: HashMap<String, Any?>) {
        val chatRoomInstance = firebaseFirestore.collection(CHATROOM).document()
        val chatRoomId = chatRoomInstance.id
        chatRoom["roomId"] = chatRoomId
        chatRoomInstance.set(chatRoom)
    }

//    fun getMessages(
//        roomId: String
//    ): Task<QuerySnapshot> =
//        firebaseFirestore.collection(MESSAGES)
//            .whereEqualTo(CHAT_ROOM_ID, roomId)
//            .get()

    fun sendMessage(messageData: HashMap<String, Any?>) {
        firebaseFirestore.collection(MESSAGES)
            .add(messageData)
    }

    fun uploadFile(
        uri: Uri,
        reference: String,
        contentType: ContentType,
        fileName: String
    ): UploadTask {
        val fileRef = firebaseStorage.reference.child("$reference/$fileName")

        val storageType = storageMetadata {
            this.contentType = "${contentType.stringValue}/*"
        }
        return fileRef.putFile(uri, storageType)
    }

    fun getListOfSamples(): Task<ListResult> =
        firebaseStorage.reference.child("samples/").listAll()

    fun uploadAudioSample(url: String): Task<ByteArray> =
        firebaseStorage.getReferenceFromUrl(url).getBytes(MEGABYTES)
}
