package com.example.sync.ui.samples

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.sync.databinding.FragmentSamplesBinding
import com.example.sync.ui.BaseFragment
import com.example.sync.utils.ContentType
import com.example.sync.utils.Utils.getName
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@AndroidEntryPoint
class SamplesFragment : BaseFragment<FragmentSamplesBinding>() {

    private val viewModel: SamplesViewModel by viewModels()

    override fun getViewBinding(): FragmentSamplesBinding =
        FragmentSamplesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getAudioContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                activity?.contentResolver?.getName(uri)
                    ?.let {
                        viewModel.uploadFile(
                            uri = uri,
                            fileName = it,
                            reference = "samples",
                            contentType = ContentType.AUDIO
                        )
                    }
            }
        val getImageContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                activity?.contentResolver?.getName(uri)
                    ?.let {
                        viewModel.uploadFile(
                            uri = uri,
                            fileName = it,
                            reference = "avatars",
                            contentType = ContentType.IMAGE
                        )
                    }
            }

        binding.uploadImageButton.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.uploadAudioButton.setOnClickListener {
            getAudioContent.launch("audio/*")
        }

        binding.playAudio.setOnClickListener { _ ->
            viewModel.audio.value?.let { it -> openDialogFragment(it) }
        }

        viewModel.progressState.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun openDialogFragment(audio: ByteArray) {
        val dialog = PlaySampleDialogFragment.newInstance(audio)
        dialog.show(childFragmentManager, null)
    }

    private fun showLoading() {
        binding.uploadAudioButton.isVisible = false
        binding.uploadImageButton.isVisible = false
        binding.playAudio.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun hideLoading() {
        binding.uploadAudioButton.isVisible = true
        binding.uploadImageButton.isVisible = true
        binding.playAudio.isVisible = true
        binding.progressBar.isVisible = false

    }

    companion object {
        fun newInstance() = SamplesFragment()
    }
}