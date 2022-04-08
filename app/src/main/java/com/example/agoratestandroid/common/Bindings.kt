package com.example.agoratestandroid.common

import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agoratestandroid.common.extensions.launchWhenStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach

fun <T> Fragment.bindAction(stateFlow: StateFlow<SingleEvent<T?>>, block: (T) -> Unit) {
    stateFlow.onEach {
        it.getContentIfNotHandled()?.let { block(it) }
    }.launchWhenStarted(viewLifecycleOwner, lifecycleScope)
}

fun bindTextTwoWay(stateFlow: MutableStateFlow<String?>, editText: EditText) {
    editText.doOnTextChanged { text, _, _, _ ->
        stateFlow.value = text.toString()
    }
}

fun <T> Fragment.bind(stateFlow: StateFlow<SingleEvent<T?>>, block: (T) -> Unit) =
    stateFlow.onEach { value ->
        value.data?.let { block(it) }
    }.launchWhenStarted(viewLifecycleOwner, lifecycleScope)

fun Fragment.bindVisible(stateFlow: Visible, view: View, asInvisible: Boolean = false) =
    stateFlow.onEach {
        if (asInvisible) {
            if (it.data) view.visibility = View.VISIBLE
            else view.visibility = View.INVISIBLE
        } else {
            view.isVisible = it.data
        }
    }.launchWhenStarted(viewLifecycleOwner, lifecycleScope)

fun Fragment.onClickListener(view: View, block: () -> Unit) {
    view.setOnClickListener { block() }
}

fun <T, TViewHolder : RecyclerView.ViewHolder?> Fragment.bindRecyclerViewAdapter(
    stateFlow: DataList<T>,
    adapter: ListAdapter<T, TViewHolder>,
    block: (() -> Unit)? = null
) = stateFlow.onEach {
    adapter.submitList(it.data) {
        if (block != null) block()
    }
}.launchWhenStarted(viewLifecycleOwner, lifecycleScope)