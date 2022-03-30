package com.example.agoratestandroid.common.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun<T> Flow<T>.launchWhenStarted(lifecycleOwner: LifecycleOwner, lifecycleCoroutineScope: LifecycleCoroutineScope){
    lifecycleCoroutineScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            this@launchWhenStarted.collect()
        }
    }
}