package com.tabnote.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val WELCOME_SCREEN = booleanPreferencesKey("welcome_screen")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val TOKEN = stringPreferencesKey("token")
    }

    val getToken : Flow<String> = context.dataStore.data
        .map {
            it[TOKEN] ?: ""
        }

    suspend fun saveToken(token:String){
        context.dataStore.edit {
            it[TOKEN]=token
        }
    }

    val getWelcomeScreen : Flow<Boolean> = context.dataStore.data
        .map {
            it[WELCOME_SCREEN] ?:true
        }

    suspend fun saveWS(boolean: Boolean){
        context.dataStore.edit {
            it[WELCOME_SCREEN]=boolean
        }
    }

    val getName: Flow<String?> = context.dataStore.data
        .map {
            it[USER_NAME] ?: ""
        }

    suspend fun saveName(s: String) {
        context.dataStore.edit {
            it[USER_NAME] = s
        }
    }

    val getID: Flow<String?> = context.dataStore.data
        .map {
            it[USER_ID] ?: ""
        }

    suspend fun saveID(s: String) {
        context.dataStore.edit {
            it[USER_ID] = s
        }
    }

    val getPassword: Flow<String?> = context.dataStore.data
        .map {
            it[USER_PASSWORD] ?: ""
        }

    suspend fun savePassWord(s: String) {
        context.dataStore.edit {
            it[USER_PASSWORD] = s
        }
    }
}