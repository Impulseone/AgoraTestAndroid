package com.example.agoratestandroid.scenes.personalChat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.agoratestandroid.models.PeerMessageItem

class MessagesAdapter : ListAdapter<PeerMessageItem, MessageViewHolder>(MessagesItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder.from(parent)

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MessagesItemCallback : DiffUtil.ItemCallback<PeerMessageItem>() {
    override fun areItemsTheSame(oldItem: PeerMessageItem, newItem: PeerMessageItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PeerMessageItem, newItem: PeerMessageItem): Boolean {
        return oldItem == newItem
    }
}