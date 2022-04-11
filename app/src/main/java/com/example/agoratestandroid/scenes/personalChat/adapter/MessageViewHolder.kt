package com.example.agoratestandroid.scenes.personalChat.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.agoratestandroid.common.extensions.setInvisible
import com.example.agoratestandroid.common.extensions.setVisible
import com.example.agoratestandroid.databinding.ItemMessageBinding
import com.example.agoratestandroid.models.PeerMessageItem
import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage

class MessageViewHolder(private val binding: ItemMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(peerMessageItem: PeerMessageItem) {
        with(binding) {
            with(peerMessageItem) {
                if (isSelf) {
                    itemLayoutR.setVisible()
                    itemLayoutL.setInvisible()
                    itemImgR.setInvisible()
                    itemMsgR.text = peerMessageItem.text
                    rtmImageMessage?.let { setImage(it, true) }
                    rtmFileMessage?.let { setFile(it, true) }
                } else {
                    itemLayoutR.setInvisible()
                    itemLayoutL.setVisible()
                    itemImgL.setInvisible()
                    itemMsgL.text = peerMessageItem.text
                    rtmImageMessage?.let { setImage(it, false) }
                    rtmFileMessage?.let { setFile(it, false) }
                }
            }
        }
    }

    private fun setImage(imageMessage: RtmImageMessage, isSelf: Boolean) {
        with(imageMessage) {
            with(binding) {
                val builder: RequestBuilder<Drawable> = Glide.with(itemView)
                    .load(thumbnail)
                    .override(thumbnailWidth, thumbnailHeight)
                if (isSelf) {
                    itemImgR.setVisible()
                    itemImgL.setInvisible()
                    builder.into(binding.itemImgR)
                } else {
                    itemImgL.setVisible()
                    itemImgR.setInvisible()
                    builder.into(binding.itemImgL)
                }
            }
        }
    }

    private fun setFile(fileMessage: RtmFileMessage, isSelf: Boolean) {
        with(fileMessage) {
            with(binding) {
                if (isSelf) {
                    itemMsgR.text = fileName
                } else {
                    itemMsgL.text = fileName
                }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): MessageViewHolder {
            return MessageViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}