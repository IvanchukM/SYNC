package com.example.sync.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun authenticate(
        email: String,
        password: String
    ): AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String): AuthResult =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()

}