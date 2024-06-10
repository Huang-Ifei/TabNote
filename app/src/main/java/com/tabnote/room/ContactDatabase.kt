package com.tabnote.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class],
    version = 2,
    exportSchema = false
)

abstract class ContactDatabase:RoomDatabase() {
    abstract val dao:ContactDao
}


