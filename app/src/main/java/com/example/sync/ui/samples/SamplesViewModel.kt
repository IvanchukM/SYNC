package com.example.sync.ui.samples

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync.repository.Repository
import com.example.sync.utils.ContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SamplesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val progressState = MutableLiveData<Boolean>()

    val audio = MutableLiveData<ByteArray>()

    init {
        downloadSamples()
    }

    fun uploadFile(uri: Uri, reference: String, contentType: ContentType, fileName: String) {
        progressState.postValue(true)
        repository.uploadFile(uri, reference, contentType, fileName)
            .addOnSuccessListener {
                progressState.postValue(false)
            }
            .addOnFailureListener {
                Log.e("TAG", "upload: $it")
            }
    }

    private fun downloadSamples() {
        progressState.postValue(true)
        repository.getListOfSamples().addOnSuccessListener { resultList ->
                resultList.items.forEach {  url ->
                    repository.uploadAudioSample(url.toString())
                        .addOnSuccessListener { sample->
                            Log.d("TAG", "downloadSamples: $sample")
                            audio.postValue(sample)
                            progressState.postValue(false)
                        }
                        .addOnFailureListener { error ->
                            Log.e("TAG", "downloadSamples: $error")
                        }
                }
        }
            .addOnFailureListener {
                Log.e("TAG", "getList: $it")
            }
    }
}
