package com.example.agoratestandroid.scenes.personalChat

import android.net.Uri
import android.os.Bundle
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
import java.io.FileOutputStream
import java.util.*

class PersonalChatFragment : BaseFragment<PersonalChatViewModel>(R.layout.scene_personal_chat) {
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    private val messagesAdapter = MessagesAdapter()

    var file: File? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.sendPhoto(navArgs.peerId, file!!.path)
            }
        }

    private val selectGalleryImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                createFileFromGallery(it)
                viewModel.sendPhoto(navArgs.peerId, file!!.path)
            }
        }

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
                    with(messageEt.text.toString()) {
                        if (isNotEmpty()) onClickSendPeerMsg(navArgs.userId, navArgs.peerId, this)
                    }
                }
                onClickListener(photoButton) {
                    onClickSendPhoto()
                }
                onClickListener(galleryButton) {
                    onClickSendGalleryPhoto()
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
                bindAction(onClickTakePhotoCommand) {
                    takePictureIntent()
                }
                bindAction(onClickTakeGalleryPhotoCommand) {
                    selectGalleryImage()
                }
            }
        }
    }

    private fun selectGalleryImage() {
        lifecycleScope.launchWhenStarted {
            selectGalleryImageResult.launch("image/*")
        }
    }

    private fun takePictureIntent() {
        lifecycleScope.launchWhenStarted {
            createFileFromCamera().let { uri ->
                takeImageResult.launch(uri)
            }
        }
    }

    private fun createFileFromCamera(): Uri {
        file = File(requireContext().filesDir, "${UUID.randomUUID()}.jpeg")
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            file!!
        )
    }

    private fun createFileFromGallery(uri: Uri) {
        file = File(requireContext().filesDir, "${UUID.randomUUID()}.jpeg")
        val stream = requireContext().contentResolver.openInputStream(uri)
        FileOutputStream(file, false).use { outputStream ->
            var read: Int
            val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
            while (stream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        }
    }
}