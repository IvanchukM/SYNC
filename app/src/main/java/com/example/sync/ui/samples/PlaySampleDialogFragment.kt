package com.example.sync.ui.samples

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sync.R
import kotlinx.android.synthetic.main.dialog_fragment_play_sample.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val AUDIO_BYTE_ARRAY = "audio"

class PlaySampleDialogFragment : DialogFragment() {

    val mediaPlayer = MediaPlayer()
    val tempMp3: File = File.createTempFile("audio", "mp3")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_play_sample, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sampleData = arguments?.getByteArray(AUDIO_BYTE_ARRAY)
        buttonPlaySample.setOnClickListener {
            sampleData?.let { it1 -> playAudio(it1) }
        }

    }

    private fun playAudio(audio: ByteArray) {

        try {
            tempMp3.deleteOnExit()
            val fileOutputStream = FileOutputStream(tempMp3)
            fileOutputStream.write(audio)
            fileOutputStream.close()

            mediaPlayer.reset()

            val fis = FileInputStream(tempMp3)
            mediaPlayer.setDataSource(fis.fd)
            mediaPlayer.prepare()
            // TODO: fix stop/pause issue
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                buttonPlaySample.text = "Pause"
            } else {
                mediaPlayer.pause()
                buttonPlaySample.text = "Play"
            }
        } catch (e: Exception) {
            Log.e("TAG", "playAudio: $e")
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
    companion object {
        fun newInstance(audio: ByteArray) = PlaySampleDialogFragment().apply {
            arguments = Bundle().apply {
                putByteArray(AUDIO_BYTE_ARRAY, audio)
            }
        }

    }
}