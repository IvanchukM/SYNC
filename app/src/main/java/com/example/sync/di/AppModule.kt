package com.example.sync.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()
}
