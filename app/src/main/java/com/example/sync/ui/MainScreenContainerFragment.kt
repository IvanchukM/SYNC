package com.example.sync.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sync.R
import com.example.sync.ui.mainScreen.MainScreenFragment


class MainScreenContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    MainScreenFragment.newInstance()
                )
                .commit()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance() = MainScreenContainerFragment()
    }
}