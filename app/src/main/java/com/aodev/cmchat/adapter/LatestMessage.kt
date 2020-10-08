package com.aodev.cmchat.adapter

import android.content.Context
import com.aodev.cmchat.R
import com.aodev.cmchat.data.ChatMessage
import com.aodev.cmchat.data.User
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.row_latest_message.view.*
import kotlinx.android.synthetic.main.row_left_chat_log.view.*

class LatestMessage(val message: ChatMessage) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.row_latest_message

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.itemView.textView_username_latest_message.text = user.userName
        viewHolder.itemView.textView_latest_message.text = message.message

//        Glide.with(context)
//            .load(user?.imagePathUrl)
//            .placeholder(R.drawable.ic_baseline_info_24)
//            .into(viewHolder.itemView.row_latest_message_imageview);

    }
}