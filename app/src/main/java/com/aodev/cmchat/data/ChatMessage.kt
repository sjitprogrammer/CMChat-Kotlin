package com.aodev.cmchat.data

data class ChatMessage(
    val id: String,
    val fromId: String,
    val toId: String,
    val message: String,
    val timeStemp: Long
){
    constructor():this("","","","",-1)
}