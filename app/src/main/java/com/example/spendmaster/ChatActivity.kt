package com.example.spendmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendmaster.MessageAdapter
import com.example.spendmaster.databinding.ActivityChatBinding
import com.example.spendmaster.Message
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""

    private var db = Firebase.firestore
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        if (chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews() {
        initRecyclerView()
        binding.sendMessageButton.setOnClickListener { sendMessage() }

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                messageAdapter.setData(listMessages)
                scrollToBottom()
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if (error == null) {
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        messageAdapter.setData(listMessages)
                        scrollToBottom()
                    }
                }
            }
    }

    private fun initRecyclerView() {
        messageAdapter = MessageAdapter(user)
        binding.messagesRecylerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }
    }

    private fun scrollToBottom() {
        val itemCount = messageAdapter.itemCount
        binding.messagesRecylerView.scrollToPosition(itemCount - 1)
    }

    private fun sendMessage() {
        val message = Message(
            message = binding.messageTextField.text.toString(),
            from = user
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)

        binding.messageTextField.setText("")
    }
}