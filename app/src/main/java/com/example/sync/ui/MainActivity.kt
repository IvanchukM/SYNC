package com.example.sync.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sync.R
import com.example.sync.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    MainScreenContainerFragment.newInstance()
                )
                .commit()
        }
    }
}
