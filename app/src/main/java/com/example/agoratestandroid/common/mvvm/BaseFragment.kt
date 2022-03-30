package com.example.agoratestandroid.common.mvvm

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.agoratestandroid.common.bindAction
import com.example.agoratestandroid.common.extensions.showToast

abstract class BaseFragment<TViewModel : BaseViewModel>(@LayoutRes lId: Int) : Fragment(lId) {
    protected abstract val viewModel: TViewModel

    protected open fun bindViewModel() {
        with(viewModel) {
            bindAction(showToastCommand) {
                showToast(it)
            }
        }
    }
}