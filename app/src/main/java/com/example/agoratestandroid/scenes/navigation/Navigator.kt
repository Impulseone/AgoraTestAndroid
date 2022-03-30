package com.example.agoratestandroid.scenes.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.agoratestandroid.R

object Navigator {

    fun popBackStack(f: Fragment) = f.findNavController().popBackStack()

    fun goToMainScreen(f: Fragment) = f.navigateClearingStack(R.id.mainFragment)

    private fun Fragment.navigateClearingStack(
        @IdRes resId: Int,
        clear: Boolean = true,
        args: Bundle? = null
    ) {
        val options = if (clear)
            NavOptions.Builder()
                .setPopUpTo(findNavController().backQueue.first().destination.id, true)
                .setLaunchSingleTop(true)
                .build()
        else null
        findNavController().navigate(resId, args, options)
    }
}
