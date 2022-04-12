package com.example.agoratestandroid.common

import kotlinx.coroutines.flow.StateFlow

typealias Visible = StateFlow<SingleEvent<Boolean>>
typealias Text = StateFlow<SingleEvent<String>>
typealias DataList<T> = StateFlow<SingleEvent<List<T>>>
typealias Data<T> = StateFlow<SingleEvent<T>>