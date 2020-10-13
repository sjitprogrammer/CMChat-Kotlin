package com.aodev.cmchat.adapter

import android.content.Context
import com.aodev.cmchat.R
import com.aodev.cmchat.data.ChatMessage
import com.aodev.cmchat.data.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.row_latest_message.view.*
import kotlinx.android.synthetic.main.row_left_chat_log.view.*

class LatestMessage(val message: ChatMessage, val context: Context) : Item<GroupieViewHolder>() {
    var user: User? = null
    override fun getLayout() = R.layout.row_latest_message

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var chatParterId: String
        if (message.fromId == FirebaseAuth.getInstance().uid) {
            chatParterId = message.toId
        } else {
            chatParterId = message.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("users/$chatParterId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                viewHolder.itemView.textView_username_latest_message.text =
                    context.getString(R.string.user_unknown)
                Glide.with(context)
                    .load(R.drawable.ic_baseline_person_24)
                    .into(viewHolder.itemView.row_latest_message_imageview);
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                viewHolder.itemView.textView_username_latest_message.text = user!!.userName
                Glide.with(context)
                    .load(user?.imagePathUrl)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(viewHolder.itemView.row_latest_message_imageview);
            }

        })

        viewHolder.itemView.textView_latest_message.text = message.message


    }
}