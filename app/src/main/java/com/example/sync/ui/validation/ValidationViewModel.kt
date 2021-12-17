package com.example.sync.ui.validation

import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync.R
import com.example.sync.repository.Repository
import com.example.sync.utils.Constants
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ValidationViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val authState = MutableLiveData<Pair<Int, Boolean>>()
    val registrationState = MutableLiveData<Pair<Int, Boolean>>()
    var job: Job? = null

    fun login(email: EditText, password: EditText) {
        job = CoroutineScope(IO).launch {
            try {
                if (validateEmail(email) and validatePassword(password)
                ) {
                    repository.authenticate(
                        email.text.toString().trim(),
                        password.text.toString().trim()
                    )
                        .let {
                            authState.postValue(Pair(R.string.login_successful, true))
                        }
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                authState.postValue(Pair(R.string.invalid_login, false))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                authState.postValue(Pair(R.string.invalid_password, false))
            } catch (e: Exception) {
                authState.postValue(Pair(R.string.error_msg, false))
            }
        }
    }

    fun register(email: EditText, password: EditText, username: EditText) {
        job = CoroutineScope(IO).launch {
            try {
                if (validateEmail(email) and validatePassword(password)
                ) {
                    repository.register(
                        email.text.toString().trim(),
                        password.text.toString().trim()
                    )
                        .let {
                            registrationState.postValue(
                                Pair(
                                    R.string.registration_successful,
                                    true
                                )
                            )
                            saveUserData(username.text.toString())
                        }
                }

            } catch (e: FirebaseAuthUserCollisionException) {
                registrationState.postValue(Pair(R.string.email_taken, false))
                Log.d("TAG", "register: $e")
            } catch (e: FirebaseAuthException) {
                Log.d("TAG", "register: $e")
                registrationState.postValue(Pair(R.string.error_msg, false))
            }
        }
    }

    private fun saveUserData(username: String) {
        val currentUser = firebaseAuth.currentUser?.uid.toString()
        val userData = hashMapOf<String, Any?>().also {
            it["uid"] = currentUser
            it["username"] = username
            it["profilePicture"] = null
        }
        repository.addUserData(currentUser, userData)
    }

    private suspend fun validateEmail(email: EditText): Boolean {
        val emailInput = email.text.toString().trim()
        return if (emailInput.isEmpty()) {
            email.setResError(R.string.empty_field_error_msg)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setResError(R.string.valid_email_address_error_msg)
            false
        } else {
            email.setResError(null)
            true
        }
    }

    private suspend fun validatePassword(password: EditText): Boolean {
        val passwordInput = password.text.toString().trim()
        return if (passwordInput.isEmpty()) {
            password.setResError(R.string.empty_field_error_msg)
            false
        } else if (!Constants.PASSWORD_REGISTRATION_PATTERN.matcher(passwordInput).matches()) {
            password.setResError(R.string.weak_password_error_msg)
            false
        } else {
            password.setResError(null)
            true
        }
    }

    private suspend fun EditText.setResError(resError: Int?) {
        withContext(Main) {
            resError?.let {
                error = context.getString(it)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}

