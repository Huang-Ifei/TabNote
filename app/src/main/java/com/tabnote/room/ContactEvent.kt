package com.tabnote.room

sealed interface ContactEvent {
    data object SaveContact:ContactEvent
    data class UpsertContact(val contact: Contact):ContactEvent
    data class SetId(val id:String):ContactEvent
    data class SetDate(val date: String):ContactEvent
    data class SetContext(val string: String):ContactEvent
    data class SetLink(val link:String):ContactEvent
    data class SetDone(val done:Boolean):ContactEvent
    data class SortContact(val sortType:SortType):ContactEvent
    data class DeleteContact(val contact: Contact):ContactEvent
}

enum class SortType{
    DATE
}
