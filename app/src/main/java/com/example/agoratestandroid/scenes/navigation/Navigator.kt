package com.example.agoratestandroid.scenes.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.agoratestandroid.R

object Navigator {

    fun goToMainScreen(f: Fragment, userId: String) = f.navigateClearingStack(
        R.id.mainFragment, true, bundleOf(
            USER_ID to userId
        )
    )

    fun goToLoginScreen(f: Fragment) = f.navigateClearingStack(R.id.loginFragment)

    fun goToChatScreen(f: Fragment, username: String, friendName: String) = f.navigateClearingStack(
        R.id.personalChatFragment, false, bundleOf(USER_ID to username, PEER_ID to friendName)
    )

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

    private const val USER_ID: String = "userId"
    private const val PEER_ID: String = "peerId"
}
