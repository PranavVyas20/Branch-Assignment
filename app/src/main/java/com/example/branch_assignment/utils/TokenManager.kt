package com.example.branch_assignment.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenManager @Inject constructor(private val dataStorePref: DataStore<Preferences>) {

    suspend fun saveToken(token: String) {
        dataStorePref.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = token
        }
    }

     suspend fun getToken(callback: (token: String?) -> Unit){
        val savedToken = dataStorePref.data.first()[PreferencesKeys.USER_TOKEN]
        callback(savedToken)
    }
}