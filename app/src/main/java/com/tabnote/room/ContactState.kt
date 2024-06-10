package com.tabnote.room

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ContactState(
    val allContacts:List<Contact> = emptyList(),
    val hisContacts:List<Contact> = emptyList(),
    val contacts:List<Contact> = emptyList(),
    val id:String = "",
    val date: String =  DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(LocalDate.now()),
    val context:String = "",
    val link:String = "",
    val done:Boolean = false,
    val sortType: SortType = SortType.DATE
)
