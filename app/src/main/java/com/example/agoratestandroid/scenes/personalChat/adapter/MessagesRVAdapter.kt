package com.example.agoratestandroid.scenes.personalChat.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agoratestandroid.models.PeerMessageItem

class MessagesRVAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages = mutableListOf<PeerMessageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder.from(parent)

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.count()

    @SuppressLint("NotifyDataSetChanged")
    fun update(peerMessageItem: PeerMessageItem){
        messages.add(peerMessageItem)
        notifyDataSetChanged()
    }
}

class MessagesAdapter : ListAdapter<PeerMessageItem, MessageViewHolder>(MessagesItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewHolder.from(parent)

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MessagesItemCallback : DiffUtil.ItemCallback<PeerMessageItem>() {
    override fun areItemsTheSame(oldItem: PeerMessageItem, newItem: PeerMessageItem): Boolean {
        return oldItem.text == newItem.text && oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: PeerMessageItem, newItem: PeerMessageItem): Boolean {
        return oldItem == newItem
    }
}