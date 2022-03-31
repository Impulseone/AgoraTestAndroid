package com.example.agoratestandroid.scenes.personalChat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.agoratestandroid.databinding.ItemMessageBinding
import com.example.agoratestandroid.models.PeerMessageItem

class MessageViewHolder(private val binding: ItemMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(peerMessageItem: PeerMessageItem) {
        with(binding) {
            if (peerMessageItem.isSelf) {
                itemLayoutL.isVisible = false
                itemMsgR.text = peerMessageItem.text
            } else {
                itemLayoutR.isVisible = false
                itemMsgL.text = peerMessageItem.text
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