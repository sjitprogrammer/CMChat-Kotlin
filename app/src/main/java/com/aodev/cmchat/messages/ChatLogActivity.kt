package com.aodev.cmchat.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aodev.cmchat.R
import com.aodev.cmchat.adapter.ChatItemLeft
import com.aodev.cmchat.adapter.ChatItemRight
import com.aodev.cmchat.data.ChatMessage
import com.aodev.cmchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "ChatLogActivity"
    }

    lateinit var user: User
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.userName
//        setUpDummyMessage()

        recyclerview_chat_logs.adapter = adapter
        listenForMessage()
        button_chat_log_send.setOnClickListener {
            PerformSendMessage()
        }
    }

    private fun listenForMessage() {
        val reference = FirebaseDatabase.getInstance().getReference("/message")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage?.message)
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatItemRight(chatMessage.message))
                    }else{
                        adapter.add(ChatItemLeft(chatMessage.message))
                    }

                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun PerformSendMessage() {
        val message = editTextText_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid.toString()
        val toId = user.uuid.toString()
        Log.d(TAG, "You send ${message}")
        val reference = FirebaseDatabase.getInstance().getReference("/message").push()
        val chatMessage = ChatMessage(
            reference.key.toString(),
            fromId,
            toId,
            message,
            System.currentTimeMillis() / 1000
        )
        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "sent message successful")
        }.addOnFailureListener {
            Log.d(TAG, "Error sent message failure!!!!!")
        }
    }

    private fun setUpDummyMessage() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatItemLeft("recieve message1"))
        adapter.add(ChatItemLeft("recieve message2"))
        adapter.add(ChatItemRight("send message1"))
        adapter.add(ChatItemLeft("recieve message3"))
        adapter.add(ChatItemRight("send message2"))
        adapter.add(ChatItemLeft("recieve message4"))
        adapter.add(ChatItemLeft("recieve message5"))
        adapter.add(ChatItemRight("send message3"))

        recyclerview_chat_logs.adapter = adapter
    }
}