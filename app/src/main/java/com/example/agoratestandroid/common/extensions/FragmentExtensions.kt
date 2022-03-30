package com.example.agoratestandroid.common.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.collectFlow(flow: Flow<T>, function: (value: T) -> Unit) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                function(it)
            }
        }
    }
}

fun Fragment.showToast(text: String?) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}