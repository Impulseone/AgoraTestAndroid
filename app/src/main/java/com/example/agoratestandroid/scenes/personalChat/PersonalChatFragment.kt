package com.example.agoratestandroid.scenes.personalChat

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.FragmentPersonalChatBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalChatFragment : Fragment(R.layout.fragment_personal_chat) {
    private val binding: FragmentPersonalChatBinding by viewBinding()
    private val viewModel: PersonalChatViewModel by viewModel()
}