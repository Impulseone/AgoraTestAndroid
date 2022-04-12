package com.example.agoratestandroid.scenes.personalChat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agoratestandroid.common.extensions.setInvisible
import com.example.agoratestandroid.common.extensions.setVisible
import com.example.agoratestandroid.databinding.ItemMessageBinding
import com.example.agoratestandroid.models.PeerMessageItem
import java.io.File

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
                } else {
                    itemLayoutR.setInvisible()
                    itemLayoutL.setVisible()
                    itemImgL.setInvisible()
                    itemMsgL.text = peerMessageItem.text
                }
                file?.apply { setFile(file, isSelf) }
            }
        }
    }

    private fun setFile(file: File, isSelf: Boolean) {
        with(file) {
            with(binding) {
                if (isSelf) {
                    itemMsgR.text = name
                } else {
                    itemMsgL.text = name
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