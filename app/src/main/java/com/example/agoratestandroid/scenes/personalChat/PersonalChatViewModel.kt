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
import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class PersonalChatViewModel(
    private val chatService: ChatService,
    private val attachmentService: AttachmentService
) : BaseViewModel() {

    val messagesList = DataList<PeerMessageItem>()
    val fileMessageReceived = TCommand<RtmFileMessage>()
    val imageMessageReceived = TCommand<RtmImageMessage>()

    private val sendMessageState = State()

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
                imageMessageReceived.setValue(it)
            }.processThrowable().launchIn(viewModelScope)

            receivedFileMessageFlow.onEach {
                fileMessageReceived.setValue(it)
            }.processThrowable().launchIn(viewModelScope)
        }
    }

    fun sendPeerMessage(fromId: String, toId: String, text: String) {
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

    fun sendPhoto(peerId: String, file: File) {
        attachmentService.sendImageMessage(peerId, file).onEach {
            when (it) {
                is LoadingResult.Success -> {
                    val updatedList = messagesList.value.data.toMutableList()
                        .apply {
                            add(
                                PeerMessageItem(
                                    isSelf = true,
                                    text = "",
                                    file = file
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

    fun sendFile(peerId: String, file: File) {
        attachmentService.sendFileMessage(peerId, file).onEach {
            when (it) {
                is LoadingResult.Success -> {
                    val updatedList = messagesList.value.data.toMutableList()
                        .apply {
                            add(
                                PeerMessageItem(
                                    isSelf = true,
                                    text = "",
                                    file = file
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

    fun saveFileToStorage(mediaId: String, file: File) {
        attachmentService.saveMediaToStorage(mediaId, file.path).onEach {
            when (it) {
                is LoadingResult.Success -> {
                    val updatedList = messagesList.value.data.toMutableList()
                        .apply {
                            add(
                                PeerMessageItem(
                                    isSelf = false,
                                    text = "",
                                    file = file
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

    override fun onCleared() {
        super.onCleared()
        chatService.stopListeningMessages(chatRtmListener)
    }
}