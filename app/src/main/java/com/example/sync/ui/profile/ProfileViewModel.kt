package com.example.sync.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseStorage: FirebaseFirestore
) :
    ViewModel() {

    var job: Job? = null

    init {
    }

//    fun sendTestData() {
//        val userData = UserData(
//            userName = "Test",
//            phoneNumber = 8800553535
//        )
//        job = CoroutineScope(IO).launch {
//            try {
//                firebaseStorage.collection("users")
//                    .document(userData.userName)
//                    .set(userData)
//            } catch (e: Exception) {
//                Log.e("exception", "sendTestData: $e ")
//            }
//        }
//    }



}