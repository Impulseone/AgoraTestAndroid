package com.example.agoratestandroid.scenes.personalChat.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agoratestandroid.models.PeerItemMessage

class MessagesAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages = mutableListOf<PeerItemMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder.from(parent)

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.count()

    @SuppressLint("NotifyDataSetChanged")
    fun update(peerItemMessage: PeerItemMessage){
        messages.add(peerItemMessage)
        notifyDataSetChanged()
    }
}