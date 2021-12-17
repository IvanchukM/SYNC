package com.example.sync.repository

import androidx.lifecycle.MutableLiveData
import com.example.sync.utils.Constants
import com.example.sync.utils.Constants.Companion.USERNAME
import com.example.sync.utils.Constants.Companion.USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun authenticate(
        email: String,
        password: String
    ): AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String): AuthResult =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()

    fun addUserData(currentUser: String, userData: HashMap<String, Any?>) {
        firebaseFirestore.collection("users").document(currentUser)
            .set(userData)
    }

     fun getUid(): String =  firebaseAuth.currentUser?.uid.toString()

    suspend fun getUsername(uid: String) =
        firebaseFirestore
            .collection(USERS)
            .document(uid)
            .get()
            .await()
            .get(USERNAME)


    fun getUsers(): Task<QuerySnapshot> = firebaseFirestore.collection(USERS).get()
}
