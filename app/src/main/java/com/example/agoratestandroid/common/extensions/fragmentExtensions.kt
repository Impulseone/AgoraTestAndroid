package com.example.agoratestandroid.common.extensions

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

fun Fragment.showSnackbar(text: String?) {
    Snackbar.make(requireActivity(), requireView(), "$text", Snackbar.LENGTH_LONG).show()
}