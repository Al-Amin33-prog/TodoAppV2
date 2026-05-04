package com.example.todoappv2.core.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.todoappv2.domain.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    companion object Keys{
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ID = stringPreferencesKey("user_id")
    }
    val userFlow: Flow<UserModel?> = dataStore.data.map { prefs ->
        val name = prefs[USER_NAME]
        val email = prefs[USER_EMAIL]
        val id = prefs[USER_ID]

        if (id == null)null
        else  UserModel(
            id = id,
            name = name,
            email = email,
            isEmailVerified = false
        )


    }
    suspend fun saveUser(user:UserModel){
        dataStore.edit{ prefs ->
            prefs[USER_NAME] = user.name?: ""
            prefs[USER_EMAIL] = user.email ?: ""
            prefs[USER_ID] = user.id

        }
    }
    suspend fun clearUser(){
        dataStore.edit { it.clear() }
    }

}