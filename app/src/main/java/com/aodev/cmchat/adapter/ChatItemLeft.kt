package com.aodev.cmchat.adapter

import com.aodev.cmchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.row_left_chat_log.view.*

data class ChatItemLeft(val message: String) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.row_left_chat_log

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.row_left_chat_log_textview.text = message
//
//        Glide.with(context)
//            .load(user?.imagePathUrl)
//            .placeholder(R.drawable.ic_baseline_info_24)
//            .into(viewHolder.itemView.row_new_message_imageview);

    }
}
