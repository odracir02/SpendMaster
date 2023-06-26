package com.example.spendmaster

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendmaster.ChatAdapter
import com.example.spendmaster.databinding.FragmentListOfChatsBinding
import com.example.spendmaster.chatDataClass
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import java.util.*

class ListOfChatsFragment : Fragment() {
    private var user = ""

    private val db = Firebase.firestore
    private lateinit var binding: FragmentListOfChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListOfChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getString("usuario") ?: ""

        if (user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews() {
        binding.newChatButton.setOnClickListener { newChat() }

        binding.listChatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(chatDataClass::class.java)

                (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if (error == null) {
                    chats?.let {
                        val listChats = it.toObjects(chatDataClass::class.java)

                        (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: chatDataClass) {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat() {
        val chatId = UUID.randomUUID().toString()
        val otherUser = binding.newChatText.text.toString()
        val users = listOf(user, otherUser)

        val chat = chatDataClass(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("users").document(user).collection("chats").document(chatId).set(chat)
        db.collection("users").document(otherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}