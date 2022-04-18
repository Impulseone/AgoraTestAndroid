package com.example.agoratestandroid.common

import kotlinx.coroutines.flow.StateFlow

typealias Visible = StateFlow<SingleEvent<Boolean>>
typealias DataList<T> = StateFlow<SingleEvent<List<T>>>