package com.example.spendmaster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spendmaster.databinding.ItemChatBinding
import com.example.spendmaster.chatDataClass

class ChatAdapter(private val chatClick: (chatDataClass) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    var chats: List<chatDataClass> = emptyList()

    fun setData(list: List<chatDataClass>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]

        holder.binding.chatNameText.text = chat.name
        holder.binding.usersTextView.text = chat.users.toString()

        holder.itemView.setOnClickListener {
            chatClick(chat)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    inner class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)
}