package com.example.agoratestandroid.scenes.personalChat

import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.common.chatManager.ChatRtmListener
import com.example.agoratestandroid.common.mvvm.BaseViewModel
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.models.PeerMessageItem
import com.example.agoratestandroid.models.State
import com.example.agoratestandroid.services.interfaces.AttachmentService
import com.example.agoratestandroid.services.interfaces.ChatService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PersonalChatViewModel(
    private val chatService: ChatService,
    private val attachmentService: AttachmentService
) : BaseViewModel() {

    val messagesList = DataList<PeerMessageItem>()

    private val sendMessageState = State()
    val onClickTakePhotoCommand = Command()
    val onClickTakeGalleryPhotoCommand = Command()

    private val chatRtmListener = ChatRtmListener()

    init {
        with(chatRtmListener) {
            chatService.listenReceivedMessages(this)
            receivedMessageFlow.onEach {
                val updatedList =
                    messagesList.value.data.toMutableList()
                        .apply { add(PeerMessageItem(false, it)) }
                messagesList.setValue(updatedList)
            }.processThrowable().launchIn(viewModelScope)
            receivedImageMessageFlow.onEach {
                val updatedList =
                    messagesList.value.data.toMutableList()
                        .apply { add(PeerMessageItem(false, "", it)) }
                messagesList.setValue(updatedList)
            }.processThrowable().launchIn(viewModelScope)
        }
    }

    fun onClickSendPeerMsg(fromId: String, toId: String, text: String) {
        chatService.sendPeerMessage(PeerMessage(fromId, toId, text)).onEach {
            when (it) {
                is LoadingResult.Success -> {
                    val updatedList = messagesList.value.data.toMutableList()
                        .apply { add(PeerMessageItem(true, it.data)) }
                    messagesList.setValue(updatedList)
                }
                is LoadingResult.Loading -> loadingState(sendMessageState)
                is LoadingResult.Failure -> failureState(it.throwable, sendMessageState)
                is LoadingResult.Empty -> {}
            }
        }.processThrowable().launchIn(viewModelScope)
    }

    fun sendPhoto(peerId: String, filePath: String) {
        attachmentService.sendImageMessage(peerId, filePath).onEach {
            when (it) {
                is LoadingResult.Success -> {
                    val updatedList = messagesList.value.data.toMutableList()
                        .apply {
                            add(
                                PeerMessageItem(
                                    isSelf = true,
                                    text = "",
                                    rtmImageMessage = it.data
                                )
                            )
                        }
                    messagesList.setValue(updatedList)
                }
                is LoadingResult.Loading -> loadingState(sendMessageState)
                is LoadingResult.Failure -> failureState(it.throwable, sendMessageState)
                is LoadingResult.Empty -> {}
            }
        }.processThrowable().launchIn(viewModelScope)
    }

    fun onClickSendPhoto() {
        onClickTakePhotoCommand.call()
    }

    fun onClickSendGalleryPhoto() {
        onClickTakeGalleryPhotoCommand.call()
    }

    override fun onCleared() {
        super.onCleared()
        chatService.stopListeningMessages(chatRtmListener)
    }
}