package com.tabnote.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    val date: String,
    val context:String,
    val link:String,
    val done:Boolean,
    @PrimaryKey
    val id:String
)


var editNum = ""

var arrDate =  Array(6){ Array(7) { "" } }
var arrShowColor =  Array(32){ 0 }