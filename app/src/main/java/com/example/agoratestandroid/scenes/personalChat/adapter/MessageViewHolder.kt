package com.example.agoratestandroid.scenes.personalChat.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.agoratestandroid.databinding.ItemMessageBinding
import com.example.agoratestandroid.models.PeerMessageItem
import io.agora.rtm.RtmImageMessage

class MessageViewHolder(private val binding: ItemMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(peerMessageItem: PeerMessageItem) {
        with(binding) {
            if (peerMessageItem.isSelf) {
                itemLayoutL.isVisible = false
                itemLayoutR.isVisible = true
                itemMsgR.text = peerMessageItem.text
                peerMessageItem.rtmImageMessage?.let { setImage(it, true) }
            } else {
                itemLayoutR.isVisible = false
                itemLayoutL.isVisible = true
                itemMsgL.text = peerMessageItem.text
                peerMessageItem.rtmImageMessage?.let { setImage(it, false) }
            }
        }
    }

    private fun setImage(imageMessage: RtmImageMessage, isSelf: Boolean) {
        val builder: RequestBuilder<Drawable> = Glide.with(itemView)
            .load(imageMessage.thumbnail)
            .override(imageMessage.thumbnailWidth, imageMessage.thumbnailHeight)
        with(binding) {
            if (isSelf) {
                builder.into(itemImgR)
                itemImgR.isVisible = true
                itemImgL.isVisible = false
            } else {
                builder.into(itemImgL)
                itemImgR.isVisible = false
                itemImgL.isVisible = true
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