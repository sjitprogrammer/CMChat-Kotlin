package com.aodev.cmchat.adapter

import android.content.Context
import com.aodev.cmchat.R
import com.aodev.cmchat.data.User
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.row_right_chat_log.view.*

class ChatItemRight(val message: String, val user: User, val context: Context) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.row_right_chat_log

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.row_right_chat_log_textview.text = message

        Glide.with(context)
            .load(user?.imagePathUrl)
            .placeholder(R.drawable.ic_baseline_info_24)
            .into(viewHolder.itemView.row_right_chat_log_imageview);

    }
}