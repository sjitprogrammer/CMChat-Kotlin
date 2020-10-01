package com.aodev.cmchat.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aodev.cmchat.R
import com.aodev.cmchat.adapter.ChatItemLeft
import com.aodev.cmchat.adapter.ChatItemRight
import com.aodev.cmchat.data.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.userName
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatItemLeft("test"))
        adapter.add(ChatItemLeft("test2"))
        adapter.add(ChatItemRight("test3"))
        adapter.add(ChatItemLeft("test4"))
        adapter.add(ChatItemRight("test5"))
        adapter.add(ChatItemLeft("test6"))
        adapter.add(ChatItemLeft("test7"))
        adapter.add(ChatItemRight("test8"))

        recyclerview_chat_logs.adapter = adapter
    }
}