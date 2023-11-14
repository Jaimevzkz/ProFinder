package com.vzkz.profinder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vzkz.profinder.data.UserFields.NICKNAME
import com.vzkz.profinder.data.UserFields.UID
import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "my_preferences"

private object UserFields {
    const val UID = "uid"
    const val NICKNAME = "nickname"
}

private const val THEME = "theme"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {
    override suspend fun saveUser(user: UserModel) {
        saveUserField(UID, user.uid)
        saveUserField(NICKNAME, user.nickname)
    }

    private suspend fun saveUserField(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getUser(): UserModel {
        val preferences = context.dataStore.data.first()
        return UserModel(
            uid = preferences[stringPreferencesKey(UID)] ?: "",
            nickname = preferences[stringPreferencesKey(NICKNAME)] ?: ""
        )
    }

    override suspend fun setAppTheme(): Boolean {
        context.dataStore.edit { preferences ->
            val value = preferences[booleanPreferencesKey(THEME)] ?: false
            preferences[booleanPreferencesKey(THEME)] = !value
        }
        return context.dataStore.data.first()[booleanPreferencesKey(THEME)] ?: false
    }

    override suspend fun getAppTheme(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(THEME)] ?: false
        }
    }
}