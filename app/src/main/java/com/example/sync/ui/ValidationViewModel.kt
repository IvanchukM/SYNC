package com.example.sync.ui

import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ValidationViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    val authState = MutableLiveData<Pair<String, Boolean>>()
    val registrationState = MutableLiveData<Pair<String, Boolean>>()
    var job: Job? = null

    fun login(email: String, password: String) {
        job = CoroutineScope(IO).launch {
            try {
                firebaseRepository.authenticate(email, password).let {
                    authState.postValue(Pair("Login Successful", true))
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                authState.postValue(Pair("Invalid login", false))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                authState.postValue(Pair("Invalid password", false))
            } catch (e: Exception) {
                authState.postValue(Pair("Error occurred. Retry later", false))
            }
        }
    }

    fun register(email: String, password: String) {
        job = CoroutineScope(IO).launch {
            try {
                firebaseRepository.register(email, password).let {
                    registrationState.postValue(Pair("Success", true))
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                registrationState.postValue(Pair("This email is already taken", false))
            } catch (e: FirebaseAuthException) {
                registrationState.postValue(Pair("Error occurred", false))
            }
        }
    }

    fun validateEmail(email: EditText): Boolean {
        val emailInput = email.text.toString().trim()
        return if (emailInput.isEmpty()) {
            email.error = "Field can't ne empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.error = "Please enter a valid email address"
            false
        } else {
            email.error = null
            true
        }
    }

    fun validatePassword(password: EditText, passwordPattern: Pattern): Boolean {
        val passwordInput = password.text.toString().trim()
        return if (passwordInput.isEmpty()) {
            password.error = "Field can not be empty"
            false
        } else if (!passwordPattern.matcher(passwordInput).matches()) {
            password.error = "Password to weak"
            false
        } else {
            password.error = null
            true
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
