package com.example.agoratestandroid.scenes.personalChat

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.BuildConfig
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.bindRecyclerViewAdapter
import com.example.agoratestandroid.common.mvvm.BaseFragment
import com.example.agoratestandroid.common.onClickListener
import com.example.agoratestandroid.common.utils.FileUtil
import com.example.agoratestandroid.databinding.ScenePersonalChatBinding
import com.example.agoratestandroid.scenes.personalChat.adapter.MessagesAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


class PersonalChatFragment : BaseFragment<PersonalChatViewModel>(R.layout.scene_personal_chat) {
    override val viewModel: PersonalChatViewModel by viewModel()
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val fileUtil: FileUtil by inject()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    private val messagesAdapter = MessagesAdapter()

    private var takePhotoContract: ActivityResultLauncher<Uri>? = null
    private var takeGalleryPhotoContract: ActivityResultLauncher<String>? = null
    private var selectFileContract: ActivityResultLauncher<String>? = null

    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFileUri()
        initContracts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    private fun initFileUri() {
        fileUri = FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            File(requireContext().filesDir, "${UUID.randomUUID()}.jpeg")
        )
    }

    private fun initContracts() {
        with(viewModel) {
            takePhotoContract =
                registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                    if (isSuccess) {
                        sendPhoto(
                            navArgs.peerId,
                            fileUtil.createFileFromUri(fileUri!!).path
                        )
                    }
                }
            takeGalleryPhotoContract =
                registerForActivityResult(ActivityResultContracts.GetContent()) {
                    if (it != null) {
                        sendPhoto(navArgs.peerId, fileUtil.createFileFromUri(it).path)
                    }
                }
            selectFileContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
                if (it != null) {
                    sendFile(navArgs.peerId, fileUtil.createFileFromUri(it))
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            with(viewModel) {
                friendId.text = navArgs.peerId
                messagesRv.adapter = messagesAdapter
                onClickListener(sendPeerMsgButton) {
                    with(messageEt.text.toString()) {
                        if (isNotEmpty()) sendPeerMessage(navArgs.userId, navArgs.peerId, this)
                    }
                }
                onClickListener(photoButton) {
                    takePhotoContract?.launch(fileUri!!)
                }
                onClickListener(galleryButton) {
                    takeGalleryPhotoContract?.launch("image/*")
                }
                onClickListener(fileButton) {
                    selectFileContract?.launch("application/*")
                }
            }
        }
    }


    override fun bindViewModel() {
        with(binding) {
            with(viewModel) {
                bindRecyclerViewAdapter(messagesList, messagesAdapter) {
                    messagesRv.scrollToPosition(messagesList.value.data.size - 1)
                    messagesList.value.data.apply { if (isNotEmpty() && last().isSelf) messageEt.text.clear() }
                }
            }
        }
    }
}