package com.example.agoratestandroid.common

import kotlinx.coroutines.flow.StateFlow

typealias Text = StateFlow<SingleEvent<String>>
typealias Visible = StateFlow<SingleEvent<Boolean>>
typealias Checkable = StateFlow<SingleEvent<Boolean>>
typealias Enabled = StateFlow<SingleEvent<Boolean>>
typealias Command = StateFlow<SingleEvent<Unit?>>
typealias TCommand<T> = StateFlow<SingleEvent<T>>
typealias Data<T> = StateFlow<SingleEvent<T>>
typealias DataList<T> = StateFlow<SingleEvent<List<T>>>
typealias Progress = StateFlow<SingleEvent<Float>>