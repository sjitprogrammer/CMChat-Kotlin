package com.aodev.cmchat.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
data class User(val uuid: String,val userName:String,val imagePathUrl :String) : Parcelable {
    constructor():this("","","")
}