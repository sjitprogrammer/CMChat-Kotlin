package com.aodev.cmchat.data

import java.util.*

data class User(val uuid: String,val userName:String,val imagePathUrl :String){
    constructor():this("","","")
}