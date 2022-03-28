package com.example.agoratestandroid
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.agora.rtm.*

class MainActivity : AppCompatActivity() {
    // Define global variables
    // EditText objects from the UI
    private var et_uid: EditText? = null
    private var et_channel_name: EditText? = null
    private var et_message_content: EditText? = null
    private var et_peer_id: EditText? = null

    // RTM uid
    private var uid: String? = null

    // RTM channel name
    private var channel_name: String? = null

    // Agora App ID
    private var AppID: String? = null

    // RTM client instance
    private var mRtmClient: RtmClient? = null

    // RTM channel instance
    private var mRtmChannel: RtmChannel? = null

    // TextView to show message records in the UI
    private var message_history: TextView? = null

    // RTM user ID of the message receiver
    private var peer_id: String? = null

    // Message content
    private var message_content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the RTM client
        try {
            AppID = baseContext.getString(R.string.app_id)
            // Initialize the RTM client
            mRtmClient = RtmClient.createInstance(
                baseContext, AppID,
                object : RtmClientListener {
                    override fun onConnectionStateChanged(state: Int, reason: Int) {
                        val text = """Connection state changed to ${state}Reason: $reason""".trimIndent()
                        writeToMessageHistory(text)
                    }

                    override fun onImageMessageReceivedFromPeer(
                        rtmImageMessage: RtmImageMessage?,
                        s: String?
                    ) {
                    }

                    override fun onFileMessageReceivedFromPeer(
                        rtmFileMessage: RtmFileMessage?,
                        s: String?
                    ) {
                    }

                    override fun onMediaUploadingProgress(
                        rtmMediaOperationProgress: RtmMediaOperationProgress?,
                        l: Long
                    ) {
                    }

                    override fun onMediaDownloadingProgress(
                        rtmMediaOperationProgress: RtmMediaOperationProgress?,
                        l: Long
                    ) {
                    }

                    override fun onTokenExpired() {}
                    override fun onPeersOnlineStatusChanged(map: Map<String?, Int?>?) {}
                    override fun onMessageReceived(rtmMessage: RtmMessage, peerId: String) {
                        val text = """Message received from $peerId Message: ${rtmMessage.getText()}"""
                        writeToMessageHistory(text)
                    }
                })
        } catch (e: Exception) {
            throw RuntimeException("RTM initialization failed!")
        }
    }

    // Button to login to the RTM system
    fun onClickLogin(v: View?) {
        et_uid = findViewById<View>(R.id.uid) as EditText
        uid = et_uid!!.text.toString()

        // Log in to the RTM system
        mRtmClient?.login(null, uid, object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {}
            override fun onFailure(errorInfo: ErrorInfo) {
                val text: CharSequence =
                    "User: $uid failed to log in to the RTM system!$errorInfo"
                val duration = Toast.LENGTH_SHORT
                runOnUiThread {
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            }
        })
    }

    // Button to join the RTM channel
    fun onClickJoin(v: View?) {
        et_channel_name = findViewById<View>(R.id.channel_name) as EditText
        channel_name = et_channel_name!!.text.toString()
        // Create a channel listener
        val mRtmChannelListener: RtmChannelListener = object : RtmChannelListener {
            override fun onMemberCountUpdated(i: Int) {}
            override fun onAttributesUpdated(list: List<RtmChannelAttribute?>?) {}
            override fun onMessageReceived(message: RtmMessage, fromMember: RtmChannelMember) {
                val text: String = message.text
                val fromUser: String = fromMember.userId
                val message_text = "Message received from $fromUser : $text\n"
                writeToMessageHistory(message_text)
            }

            override fun onImageMessageReceived(
                rtmImageMessage: RtmImageMessage?,
                rtmChannelMember: RtmChannelMember?
            ) {
            }

            override fun onFileMessageReceived(
                rtmFileMessage: RtmFileMessage?,
                rtmChannelMember: RtmChannelMember?
            ) {
            }

            override fun onMemberJoined(member: RtmChannelMember?) {}
            override fun onMemberLeft(member: RtmChannelMember?) {}
        }
        try {
            // Create an RTM channel
            mRtmChannel = mRtmClient?.createChannel(channel_name, mRtmChannelListener)
        } catch (e: RuntimeException) {
        }
        // Join the RTM channel
        mRtmChannel?.join(object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {}
            override fun onFailure(errorInfo: ErrorInfo) {
                val text: CharSequence =
                    "User: $uid failed to join the channel!$errorInfo"
                val duration = Toast.LENGTH_SHORT
                runOnUiThread {
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            }
        })
    }

    // Button to log out of the RTM system
    fun onClickLogout(v: View?) {
        // Log out of the RTM system
        mRtmClient?.logout(null)
    }

    // Button to leave the RTM channel
    fun onClickLeave(v: View?) {
        // Leave the RTM channel
        mRtmChannel?.leave(null)
    }

    // Button to send peer-to-peer message
    fun onClickSendPeerMsg(v: View?) {
        et_message_content = findViewById(R.id.msg_box)
        message_content = et_message_content?.text.toString()
        et_peer_id = findViewById(R.id.peer_name)
        peer_id = et_peer_id?.text.toString()

        // Create RTM message instance
        val message: RtmMessage = mRtmClient!!.createMessage()
        message.setText(message_content)
        val option = SendMessageOptions()
        option.enableOfflineMessaging = true

        // Send message to peer
        mRtmClient?.sendMessageToPeer(peer_id, message, option, object : ResultCallback<Void?> {
            override fun onSuccess(aVoid: Void?) {
                val text = """Message sent from $uid To $peer_id ： ${message.getText()}"""
                writeToMessageHistory(text)
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                val text =
                    "Message fails to send from $uid To $peer_id Error ： $errorInfo\n"
                writeToMessageHistory(text)
            }
        })
    }

    // Button to send channel message
    fun onClickSendChannelMsg(v: View?) {
        et_message_content = findViewById(R.id.msg_box)
        message_content = et_message_content?.text.toString()

        // Create RTM message instance
        val message: RtmMessage = mRtmClient!!.createMessage()
        message.text = message_content

        // Send message to channel
        mRtmChannel?.sendMessage(message, object : ResultCallback<Void?> {
            override fun onSuccess(aVoid: Void?) {
                val text = """Message sent to channel ${
                    mRtmChannel?.id.toString()
                } : ${message.text}"""
                writeToMessageHistory(text)
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                val text = """Message fails to send to channel ${
                    mRtmChannel?.id.toString()
                } Error: $errorInfo"""
                writeToMessageHistory(text)
            }
        })
    }

    // Write message records to the TextView
    fun writeToMessageHistory(record: String?) {
        message_history = findViewById(R.id.message_history)
        message_history?.append(record)
    }
}