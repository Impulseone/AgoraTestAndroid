package com.example.agoratestandroid.scenes.personalChat

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.BuildConfig
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.bindAction
import com.example.agoratestandroid.common.bindRecyclerViewAdapter
import com.example.agoratestandroid.common.mvvm.BaseFragment
import com.example.agoratestandroid.common.onClickListener
import com.example.agoratestandroid.databinding.ScenePersonalChatBinding
import com.example.agoratestandroid.scenes.personalChat.adapter.MessagesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PersonalChatFragment : BaseFragment<PersonalChatViewModel>(R.layout.scene_personal_chat) {
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    private val messagesAdapter = MessagesAdapter()

    var file: File? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.sendPhoto(navArgs.peerId, file!!.path)
//                latestTmpUri?.let { uri ->
//                    uri.path?.let { viewModel.sendPhoto(navArgs.peerId, file!!.path) }
//                }
            }
        }

//    private var latestTmpUri: Uri? = null

    override val viewModel: PersonalChatViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    private fun initViews() {
        with(binding) {
            with(viewModel) {
                friendId.text = navArgs.peerId
                messagesRv.adapter = messagesAdapter
                onClickListener(sendPeerMsgButton) {
                    val fromId = navArgs.userId
                    val toId = navArgs.peerId
                    val messageText = binding.messageEt.text.toString()
                    onClickSendPeerMsg(fromId, toId, messageText)
                }
                onClickListener(attachmentButton) {
                    onClickSendAttachment()
                }
            }
        }
    }


    override fun bindViewModel() {
        with(binding) {
            with(viewModel) {
                bindRecyclerViewAdapter(messagesList, messagesAdapter) {
                    messagesRv.scrollToPosition(messagesList.value.data.size - 1)
                    if (messagesList.value.data.isNotEmpty() && messagesList.value.data.last().isSelf) messageEt.text.clear()
                }
                bindAction(onClickAttachmentCommand) {
                    dispatchTakePictureIntent()
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
//                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        file = File(requireContext().filesDir, "picFromCamera.jpeg")
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            file!!
        )
    }
}