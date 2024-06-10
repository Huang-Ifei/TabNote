package com.tabnote.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface  ContactDao {
    @Upsert
    suspend fun insertContact(contact: Contact)

    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact")
    fun getAllContact():Flow<List<Contact>>

    @Query("SELECT * FROM contact where done = 0 ORDER BY date ASC")
    fun getContactByDate():Flow<List<Contact>>

    @Query("SELECT * FROM contact where done = 1 ORDER BY date ASC")
    fun getHisContactByDate():Flow<List<Contact>>

}
