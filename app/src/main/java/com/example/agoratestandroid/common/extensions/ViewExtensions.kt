package com.example.agoratestandroid.common.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.setVisible(){
    this.isVisible = true
}

fun View.setInvisible(){
    this.isVisible = false
}